package com.makecity.client.presentation.problem

import android.os.Parcelable
import com.makecity.client.app.AppScreens
import com.makecity.client.data.problem.ProblemDetail
import com.makecity.client.data.task.FavoriteType
import com.makecity.client.data.task.Task
import com.makecity.client.domain.map.TaskPointsInteractor
import com.makecity.client.presentation.comments.CommentsScreenData
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
) : Parcelable


// Actions
sealed class ProblemAction : ActionView {
	object LoadProblem : ProblemAction()
	object ShowMoreComments : ProblemAction()
	data class ChangeFavorite(
		val task: Task
	) : ProblemAction()

	data class CreateComment(
		val text: String
	) : ProblemAction()
}


// State
data class ProblemViewState(
	override val screenState: PrimaryViewState = PrimaryViewState.Loading,
	override val connectionState: ConnectionState = ConnectionState.Unknown,
	val problemDetail: ProblemDetail? = null
) : ViewState, ViewStatePluginConnection


// Reducer
interface ProblemReducer : StatementReducer<ProblemViewState, ProblemAction>


// View Model
class ProblemViewModel @Inject constructor(
	private val router: Router,
	private val problemData: ProblemData,
	private val interactor: TaskPointsInteractor,
	override val disposables: CompositeDisposable = CompositeDisposable()
) : ReactiveViewModel(), ProblemReducer {

	override val viewState = StateLiveData.create(ProblemViewState())

	override fun reduce(action: ProblemAction) {
		when (action) {
			is ProblemAction.LoadProblem -> {
				interactor.loadProblemComments(problemData.id)
					.bindSubscribe(onSuccess = {
						viewState.updateValue {
							copy(screenState = PrimaryViewState.Data, problemDetail = it)
						}
					}, onError = {
						it.printStackTrace()
					})
			}
			is ProblemAction.ShowMoreComments -> {
				router.navigateTo(AppScreens.COMMENTS_SCREEN_KEY, CommentsScreenData(problemData.id))
			}
			is ProblemAction.ChangeFavorite -> interactor
				.changeFavorite(
					action.task.id,
					if (action.task.isLiked) FavoriteType.COMMON else FavoriteType.LIKE
				).bindSubscribe(onSuccess = { _ ->
					state.problemDetail?.task?.apply {
						copy(isLiked = !isLiked)
					}?.let {
						viewState.updateValue {
							val newDetails = problemDetail?.copy(task = it)
							copy(problemDetail = newDetails)
						}
					}
				})
			is ProblemAction.CreateComment -> interactor
				.createComment(problemData.id, action.text)
				.andThen(interactor.loadProblemComments(problemData.id))
				.bindSubscribe(onSuccess = {
					viewState.updateValue {
						copy(screenState = PrimaryViewState.Data, problemDetail = it)
					}
				})
		}
	}
}