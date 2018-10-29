package com.makecity.client.presentation.feed

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.View
import com.makecity.client.R
import com.makecity.client.app.AppInjector
import com.makecity.client.presentation.lists.TaskAdapter
import com.makecity.core.extenstion.calculateDiffs
import com.makecity.core.extenstion.isVisible
import com.makecity.core.presentation.screen.ToolbarConfig
import com.makecity.core.presentation.screen.ToolbarScreen
import com.makecity.core.presentation.state.PrimaryViewState
import com.makecity.core.presentation.view.StatementFragment
import com.makecity.core.utils.image.ImageManager
import kotlinx.android.synthetic.main.fragment_feed.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

typealias FeedStatement = StatementFragment<FeedReducer, FeedViewState, FeedAction>

class FeedFragment : FeedStatement(), ToolbarScreen {

	companion object {
		fun newInstance() = FeedFragment()
	}

	@Inject
	lateinit var imageManager: ImageManager
	private lateinit var adapter: TaskAdapter

	override val layoutId: Int = R.layout.fragment_feed

	override fun getToolbar(): Toolbar = toolbar

	override fun onInject() = AppInjector.inject(this)

	override fun onViewCreatedBeforeRender(savedInstanceState: Bundle?) {
		setupToolbarWith(requireActivity(), ToolbarConfig(
			title = getString(R.string.feed),
			isDisplayHomeButton = true
		))

		feed_recycler.layoutManager = LinearLayoutManager(context)

		adapter = TaskAdapter(imageManager) {
			reducer.reduce(ShowProblemDetails(it.id))
		}

		feed_recycler.adapter = adapter

		feed_refresh.setOnRefreshListener {
			reducer.reduce(LoadTasksAction)
		}
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		reducer.reduce(LoadTasksAction)
	}

	override fun render(state: FeedViewState) {
		when (state.screenState) {
			is PrimaryViewState.Data -> {
				feed_progress.isVisible = false
				feed_recycler.isVisible = true
				feed_refresh.isRefreshing = false
				adapter.calculateDiffs(state.tasks)
			}
			is PrimaryViewState.Loading -> {
				feed_progress.isVisible = true
				feed_recycler.isVisible = false
				feed_refresh.isRefreshing = false
			}
		}
	}
}