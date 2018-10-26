package com.makecity.client.presentation.restore

import com.makecity.client.app.AppScreens
import com.makecity.client.data.temp_problem.TempProblem
import com.makecity.client.data.temp_problem.TempProblemDataSource
import com.makecity.client.presentation.camera.CameraScreenData
import com.makecity.client.presentation.category.CategoryScreenData
import com.makecity.client.presentation.category.CategoryType
import com.makecity.client.presentation.create_problem.CreateProblemData
import com.makecity.client.presentation.create_problem.ProblemCreatingType
import com.makecity.client.presentation.description.DescriptionScreenData
import com.makecity.client.presentation.map_address.MapAddressScreenData
import com.makecity.core.data.Presentation
import com.makecity.core.presentation.state.PrimaryViewState
import com.makecity.core.presentation.state.StateLiveData
import com.makecity.core.presentation.state.ViewState
import com.makecity.core.presentation.viewmodel.ActionView
import com.makecity.core.presentation.viewmodel.BaseViewModel
import com.makecity.core.presentation.viewmodel.StatementReducer
import com.makecity.core.utils.ReactiveActions
import io.reactivex.disposables.CompositeDisposable
import ru.terrakok.cicerone.Router

// State
@Presentation
data class RestoreViewState(
	override val screenState: PrimaryViewState = PrimaryViewState.Loading
) : ViewState


// Action
sealed class RestoreAction: ActionView {
	object RestoreAllow : RestoreAction()
	object RestoreDeny: RestoreAction()
	object RestoreDetails: RestoreAction()
}


// Reducer
interface RestoreReducer: StatementReducer<RestoreViewState, RestoreAction>


// ViewModel
class RestoreViewModel(
	private val router: Router,
	private val tempProblemDataSource: TempProblemDataSource
) : BaseViewModel(), RestoreReducer, ReactiveActions {

	override val disposables: CompositeDisposable = CompositeDisposable()

	override val viewState: StateLiveData<RestoreViewState> = StateLiveData.create(RestoreViewState())

	override fun reduce(action: RestoreAction) {
		when (action) {
			is RestoreAction.RestoreDeny -> tempProblemDataSource
				.deleteAll()
				.bindSubscribe(onSuccess = {
					router.replaceScreen(AppScreens.CAMERA_SCREEN_KEY, CameraScreenData(ProblemCreatingType.NEW))
				})

			is RestoreAction.RestoreAllow -> tempProblemDataSource
				.getTempProblem()
				.bindSubscribe(onSuccess = ::restoreTempProblem)

			is RestoreAction.RestoreDetails -> router.navigateTo(
				AppScreens.CREATE_PROBLEM_SCREEN_KEY,
				CreateProblemData(canEdit = false)
			)
		}
	}

	private fun restoreTempProblem(tempProblem: TempProblem) = tempProblem.run {
		when {
			latitude > 0.0 || longitude > 0.0 -> router.replaceScreen(
				AppScreens.CREATE_PROBLEM_SCREEN_KEY,
				CreateProblemData(canEdit = true)
			)
			description.isNotEmpty() -> router.replaceScreen(
				AppScreens.MAP_ADDRESS_SCREEN_KEY,
				MapAddressScreenData(ProblemCreatingType.NEW)
			)

			optionId > 0 && categoryId > 0 -> router.replaceScreen(
				AppScreens.DESCRIPTION_SCREEN_KEY,
				DescriptionScreenData(ProblemCreatingType.NEW)
			)

			images.isNotEmpty() -> router.replaceScreen(
				AppScreens.CATEGORY_SCREEN_KEY,
				CategoryScreenData(CategoryType.CATEGORY, ProblemCreatingType.NEW)
			)

			else -> router.replaceScreen(
				AppScreens.CAMERA_SCREEN_KEY,
				CameraScreenData(ProblemCreatingType.NEW)
			)
		}
	}
}