package com.makecity.client.presentation.comments

import com.makecity.client.data.comments.Comment
import com.makecity.client.data.comments.CommentsDataSource
import com.makecity.core.data.Presentation
import com.makecity.core.extenstion.concat
import com.makecity.core.plugin.connection.ConnectionProvider
import com.makecity.core.plugin.connection.ConnectionState
import com.makecity.core.plugin.connection.ReducerPluginConnection
import com.makecity.core.presentation.list.paging.PagingActionsAdapter
import com.makecity.core.presentation.list.paging.PagingDataDelegate
import com.makecity.core.presentation.list.paging.PagingState
import com.makecity.core.presentation.state.PrimaryViewState
import com.makecity.core.presentation.state.StateLiveData
import com.makecity.core.presentation.state.ViewState
import com.makecity.core.presentation.viewmodel.ActionView
import com.makecity.core.presentation.viewmodel.BaseViewModel
import com.makecity.core.presentation.viewmodel.StatementReducer
import io.reactivex.disposables.CompositeDisposable
import ru.terrakok.cicerone.Router


// State
@Presentation
data class CommentsViewState(
	override val screenState: PrimaryViewState = PrimaryViewState.Loading,
	val comments: List<Comment> = emptyList()
) : ViewState


// Action
sealed class CommentsAction: ActionView {
	object LoadComments: CommentsAction()
}


// Reducer
interface CommentsReducer: StatementReducer<CommentsViewState, CommentsAction> {
	val pagingAdapter: PagingActionsAdapter
}


// ViewModel
class CommentsViewModel(
	private val router: Router,
	override val pagingAdapter: PagingActionsAdapter,
	private val commentsDataSource: CommentsDataSource,
	override val connectionProvider: ConnectionProvider,
	override val disposables: CompositeDisposable = CompositeDisposable()
) : BaseViewModel(), CommentsReducer, ReducerPluginConnection, PagingDataDelegate {

	override val viewState: StateLiveData<CommentsViewState> = StateLiveData.create(CommentsViewState())

	override fun reduce(action: CommentsAction) {

	}

	// IMPLEMENT - ConnectionPlugin
	override fun onChangeConnection(connectionState: ConnectionState) {

	}

	override fun onLoadPage(state: PagingState, result: (Int) -> Unit) {
		commentsDataSource.getComments(state.pageCounts + 1)
			.bindSubscribe(onSuccess = {
				result(it.size)
				viewState.updateValue {
					copy(screenState = PrimaryViewState.Data, comments = comments.concat(it))
				}
			}, onError = {
				viewState.updateValue {
					copy(screenState = PrimaryViewState.Error(it))
				}
				it.printStackTrace()
				result(0)
			})
	}
}