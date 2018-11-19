package com.makecity.client.presentation.comments

import android.os.Bundle
import android.os.Parcel
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import com.makecity.client.R
import com.makecity.client.app.AppInjector
import com.makecity.client.presentation.lists.CommentsAdapter
import com.makecity.core.extenstion.calculateDiffs
import com.makecity.core.extenstion.withArguments
import com.makecity.core.presentation.list.paging.PagingLinearScrollObserver
import com.makecity.core.presentation.list.paging.RecyclerPagingScrollObserver
import com.makecity.core.presentation.screen.ToolbarConfig
import com.makecity.core.presentation.screen.ToolbarScreen
import com.makecity.core.presentation.state.PrimaryViewState
import com.makecity.core.presentation.view.StatementFragment
import com.makecity.core.utils.image.ImageManager
import kotlinx.android.synthetic.main.fragment_comments.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject


typealias CommentsStatement = StatementFragment<CommentsReducer, CommentsViewState, CommentsAction>


class CommentsFragment : CommentsStatement(), ToolbarScreen {

	companion object {
		private const val ARGUMENT_COMMENTS_DATA = "ARGUMENT_COMMENTS_DATA"
		fun newInstance(data: CommentsScreenData) = CommentsFragment()
			.withArguments {
				putParcelable(ARGUMENT_COMMENTS_DATA, data)
			}
	}

	@Inject
	lateinit var imageManager: ImageManager

	override val layoutId: Int = R.layout.fragment_comments

	private lateinit var commentsAdapter: CommentsAdapter
	private lateinit var scrollObserver: RecyclerPagingScrollObserver

	override fun onInject() = AppInjector.inject(this, getArgument(ARGUMENT_COMMENTS_DATA))

	override fun getToolbar(): Toolbar = toolbar

	override fun onViewCreatedBeforeRender(savedInstanceState: Bundle?) {
		setupToolbarWith(requireActivity(), ToolbarConfig(
			title = getString(R.string.comments),
			isDisplayHomeButton = true,
			isEnableHomeButton = true
		))

		LinearLayoutManager(context).apply {
			comments_list.layoutManager = this
			scrollObserver = PagingLinearScrollObserver(this)
		}

		commentsAdapter = CommentsAdapter(imageManager = imageManager) {

		}

		comments_list.apply {
			adapter = commentsAdapter
			addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
			scrollObserver.addObserver(reducer.pagingAdapter)
			addOnScrollListener(scrollObserver)
		}
	}

	override fun onDestroyView() {
		super.onDestroyView()

		comments_list.removeOnScrollListener(scrollObserver)
		scrollObserver.removeObserver(reducer.pagingAdapter)
	}

	override fun render(state: CommentsViewState) {
		when (state.screenState) {
			is PrimaryViewState.Data -> commentsAdapter.calculateDiffs(state.comments)
			is PrimaryViewState.Loading -> {}
			is PrimaryViewState.Error -> {}
		}
	}
}

