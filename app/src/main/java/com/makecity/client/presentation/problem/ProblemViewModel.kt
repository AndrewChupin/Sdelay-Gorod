package com.makecity.client.presentation.problem

import android.os.Parcelable
import com.makecity.client.data.problem.ProblemDetail
import com.makecity.client.domain.map.TaskPointsInteractor
import com.makecity.core.plugin.connection.ConnectionState
import com.makecity.core.plugin.connection.ViewStatePluginConnection
import com.makecity.core.presentation.state.PrimaryViewState
import com.makecity.core.presentation.state.StateLiveData
import com.makecity.core.presentation.state.ViewState
import com.makecity.core.presentation.viewmodel.ActionView
import com.makecity.core.presentation.viewmodel.ReactiveViewModel
import com.makecity.core.presentation.viewmodel.StatementReducer
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.parcel.Parcelize
import ru.terrakok.cicerone.Router
import javax.inject.Inject


// Data
@Parcelize
data class ProblemData(
	val id: Long
): Parcelable


// Actions
sealed class ProblemAction: ActionView
object LoadProblemAction: ProblemAction()


// State
data class ProblemViewState(
	override val screenState: PrimaryViewState = PrimaryViewState.Loading,
	override val connectionState: ConnectionState = ConnectionState.Unknown,
	val problemDetail: ProblemDetail? = null
): ViewState, ViewStatePluginConnection


// Reducer
interface ProblemReducer: StatementReducer<ProblemViewState, ProblemAction>


// View Model
class ProblemViewModel @Inject constructor(
	private val router: Router,
	private val problemData: ProblemData,
	private val interactor: TaskPointsInteractor,
	override val disposables: CompositeDisposable = CompositeDisposable()
) : ReactiveViewModel(), ProblemReducer {

	override val viewState = StateLiveData.create(ProblemViewState())

	override fun reduce(action: ProblemAction) {
		if (action is LoadProblemAction) {
			interactor.loadProblemComments(problemData.id)
				.bindSubscribe(onSuccess = {
					viewState.updateValue {
						copy(screenState = PrimaryViewState.Data, problemDetail = it)
					}
				}, onError = {
					it.printStackTrace()
				})
		}
	}
}