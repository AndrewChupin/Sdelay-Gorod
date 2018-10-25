package com.makecity.client.presentation.create_problem


import com.makecity.client.app.AppScreens
import com.makecity.client.data.temp_problem.TempProblem
import com.makecity.client.data.temp_problem.TempProblemDataSource
import com.makecity.client.presentation.camera.CameraScreenData
import com.makecity.client.presentation.category.CategoryScreenData
import com.makecity.client.presentation.category.CategoryType
import com.makecity.client.presentation.description.DescriptionScreenData
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
import io.reactivex.disposables.CompositeDisposable
import ru.terrakok.cicerone.Router

// Data
enum class ProblemPreviewDataType {
	DESCRIPTION, PHOTO, INFO, LOCATION
}

enum class ProblemCreatingType {
	NEW, EDIT
}

// State
@Presentation
data class CreateProblemViewState(
	override val screenState: PrimaryViewState = PrimaryViewState.Loading,
	val tempProblem: TempProblem? = null
) : ViewState


// Action
sealed class CreateProblemAction: ActionView {
	data class ChangePreviewData(
		val previewDataType: ProblemPreviewDataType
	) : CreateProblemAction()

	object LoadPreview : CreateProblemAction()

	object ApproveProblem : CreateProblemAction()
}


// Reducer
interface CreateProblemReducer: StatementReducer<CreateProblemViewState, CreateProblemAction>


// ViewModel
class CreateProblemViewModel(
	private val router: Router,
	private val dataSource: TempProblemDataSource,
	override val connectionProvider: ConnectionProvider,
	override val disposables: CompositeDisposable = CompositeDisposable()
) : BaseViewModel(), CreateProblemReducer, ReducerPluginConnection {

	override val viewState: StateLiveData<CreateProblemViewState> = StateLiveData.create(CreateProblemViewState())

	override fun reduce(action: CreateProblemAction) {
		when (action) {
			is CreateProblemAction.ChangePreviewData -> editDataBy(action.previewDataType)
			is CreateProblemAction.ApproveProblem -> {}
			is CreateProblemAction.LoadPreview -> dataSource.getTempProblem()
				.bindSubscribe(onSuccess = {
					viewState.updateValue {
						copy(screenState = PrimaryViewState.Data, tempProblem = it)
					}
				})
		}
	}

	private fun editDataBy(type: ProblemPreviewDataType) = when (type) {
		ProblemPreviewDataType.DESCRIPTION -> router.navigateTo(
			AppScreens.DESCRIPTION_SCREEN_KEY,
			DescriptionScreenData(ProblemCreatingType.EDIT)
		)

		ProblemPreviewDataType.INFO -> router.navigateTo(
			AppScreens.CATEGORY_SCREEN_KEY,
			CategoryScreenData(CategoryType.CATEGORY, ProblemCreatingType.EDIT)
		)

		ProblemPreviewDataType.LOCATION -> router.navigateTo(
			AppScreens.MAP_ADDRESS_SCREEN_KEY,
			MapAddressScreenData(ProblemCreatingType.EDIT)
		)

		ProblemPreviewDataType.PHOTO -> router.navigateTo(
			AppScreens.CAMERA_SCREEN_KEY,
			CameraScreenData(ProblemCreatingType.EDIT)
		)
	}

	// IMPLEMENT - ConnectionPlugin
	override fun onChangeConnection(connectionState: ConnectionState) {

	}
}