package com.makecity.client.presentation.description

import android.os.Parcelable
import com.makecity.client.app.AppScreens
import com.makecity.client.data.temp_problem.TempProblemDataSource
import com.makecity.client.presentation.create_problem.ProblemCreatingType
import com.makecity.client.presentation.map_address.MapAddressScreenData
import com.makecity.core.data.Presentation
import com.makecity.core.plugin.connection.ConnectionProvider
import com.makecity.core.plugin.connection.ConnectionState
import com.makecity.core.plugin.connection.ReducerPluginConnection
import com.makecity.core.presentation.state.PrimaryViewState
import com.makecity.core.presentation.state.StateLiveData
import com.makecity.core.presentation.state.ViewState
import com.makecity.core.presentation.viewmodel.ActionView
import com.makecity.core.presentation.viewmodel.BaseViewModel
import com.makecity.core.presentation.viewmodel.StatementReducer
import com.makecity.core.utils.Symbols.EMPTY
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.parcel.Parcelize
import ru.terrakok.cicerone.Router


// Data
@Parcelize
data class DescriptionScreenData(
	val problemCreatingType: ProblemCreatingType
): Parcelable


// State
@Presentation
data class DescriptionViewState(
	override val screenState: PrimaryViewState = PrimaryViewState.Loading,
	val description: String = EMPTY
) : ViewState


// Action
sealed class DescriptionAction: ActionView {
	data class DescriptionComplete(
		val description: String
	): DescriptionAction()
	object CheckData: DescriptionAction()
}


// Reducer
interface DescriptionReducer: StatementReducer<DescriptionViewState, DescriptionAction>


// ViewModel
class DescriptionViewModel(
	private val router: Router,
	private val data: DescriptionScreenData,
	private val tempProblemDataSource: TempProblemDataSource,
	override val connectionProvider: ConnectionProvider
) : BaseViewModel(), DescriptionReducer, ReducerPluginConnection {

	override val disposables: CompositeDisposable = CompositeDisposable()
	override val viewState: StateLiveData<DescriptionViewState> = StateLiveData.create(DescriptionViewState())

	override fun reduce(action: DescriptionAction) {
		when (action) {
			is DescriptionAction.DescriptionComplete -> tempProblemDataSource.getTempProblem()
				.map { it.copy(description = action.description) }
				.flatMapCompletable(tempProblemDataSource::saveTempProblem)
				.bindSubscribe(onSuccess = ::navigateComplete)

			is DescriptionAction.CheckData -> tempProblemDataSource.getTempProblem()
				.bindSubscribe(onSuccess = {
					viewState.updateValue { copy(screenState = PrimaryViewState.Data, description = it.description) }
				})
		}
	}

	private fun navigateComplete() {
		if (data.problemCreatingType == ProblemCreatingType.NEW) {
			router.navigateTo(
				AppScreens.MAP_ADDRESS_SCREEN_KEY,
				MapAddressScreenData(ProblemCreatingType.NEW)
			)
		} else {
			router.exit()
		}
	}

	// IMPLEMENT - ConnectionPlugin
	override fun onChangeConnection(connectionState: ConnectionState) {

	}
}