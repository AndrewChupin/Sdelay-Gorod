package com.makecity.client.presentation.feed

import com.makecity.client.app.AppScreens
import com.makecity.client.data.task.FavoriteType
import com.makecity.client.data.task.Task
import com.makecity.client.domain.map.TaskPointsInteractor
import com.makecity.client.presentation.problem.ProblemData
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
import com.makecity.core.utils.log
import io.reactivex.disposables.CompositeDisposable
import ru.terrakok.cicerone.Router


// State
@Presentation
data class FeedViewState(
	override val screenState: PrimaryViewState = PrimaryViewState.Loading,
	val tasks: List<Task> = emptyList()
) : ViewState


// Action
sealed class FeedAction : ActionView {

	object LoadTasksAction : FeedAction()
	object RefreshTasksAction : FeedAction()
	data class ShowProblemDetails(
		val problemId: Long
	) : FeedAction()

	data class ChangeFavorite(
		val task: Task
	) : FeedAction()
}


// Reducer
interface FeedReducer : StatementReducer<FeedViewState, FeedAction>


// ViewModel
class FeedViewModel(
	private val router: Router,
	private val interactor: TaskPointsInteractor,
	override val connectionProvider: ConnectionProvider,
	override val disposables: CompositeDisposable = CompositeDisposable()
) : BaseViewModel(), FeedReducer, ReducerPluginConnection {

	override val viewState: StateLiveData<FeedViewState> = StateLiveData.create(FeedViewState())

	override fun reduce(action: FeedAction) {
		when (action) {
			is FeedAction.LoadTasksAction -> interactor.getProblems()
				.bindSubscribe(
					onSuccess = {
						viewState.updateValue {
							copy(screenState = PrimaryViewState.Data, tasks = it)
						}
					}
				)
			is FeedAction.RefreshTasksAction -> interactor.refreshProblems()
				.bindSubscribe(
					onSuccess = {
						viewState.updateValue {
							copy(screenState = PrimaryViewState.Data, tasks = it)
						}
					}
				)
			is FeedAction.ShowProblemDetails -> router.navigateTo(AppScreens.PROBLEM_SCREEN_KEY, ProblemData(action.problemId))
			is FeedAction.ChangeFavorite -> interactor
				.changeFavorite(action.task.id,
					if (action.task.isLiked) FavoriteType.COMMON else FavoriteType.LIKE
				)
				.bindSubscribe(onSuccess = {
					viewState.updateValue {
						copy(tasks = tasks.map {
							if (it.id == action.task.id) it.copy(isLiked = !it.isLiked)
							else it
						})
					}
				}, onError = {
					log(it)
				})
		}
	}

	override fun onChangeConnection(connectionState: ConnectionState) {

	}
}