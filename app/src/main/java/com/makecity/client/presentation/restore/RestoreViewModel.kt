package com.makecity.client.presentation.restore

import com.makecity.client.app.AppScreens
import com.makecity.client.data.temp_problem.TempProblem
import com.makecity.client.data.temp_problem.TempProblemDataSource
import com.makecity.client.presentation.camera.CameraScreenData
import com.makecity.client.presentation.category.CategoryScreenData
import com.makecity.client.presentation.category.CategoryType
import com.makecity.client.presentation.create_problem.ProblemCreatingType
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
			is RestoreAction.RestoreAllow -> tempProblemDataSource
				.deleteAll()
				.bindSubscribe(onSuccess = {
					router.navigateTo(AppScreens.CAMERA_SCREEN_KEY, CameraScreenData(ProblemCreatingType.NEW))
				})
			is RestoreAction.RestoreDeny -> tempProblemDataSource
				.getTempProblem()
				.bindSubscribe(onSuccess = ::restoreTempProblem)
		}
	}

	private fun restoreTempProblem(tempProblem: TempProblem) = tempProblem.run {
		when {
			latitude == 0.0 || longitude == 0.0 -> router.navigateTo(
				AppScreens.CREATE_PROBLEM_SCREEN_KEY,
				CameraScreenData(ProblemCreatingType.NEW)
			)

			description.isEmpty() -> router.navigateTo(
				AppScreens.DESCRIPTION_SCREEN_KEY,
				CameraScreenData(ProblemCreatingType.NEW)
			)

			optionId < 0 || categoryId < 0 -> router.navigateTo(
				AppScreens.CATEGORY_SCREEN_KEY,
				CategoryScreenData(CategoryType.CATEGORY, ProblemCreatingType.NEW)
			)

			else -> router.navigateTo(
				AppScreens.CAMERA_SCREEN_KEY,
				CameraScreenData(ProblemCreatingType.NEW)
			)
		}
	}
}