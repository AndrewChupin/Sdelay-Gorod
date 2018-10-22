package com.makecity.client.presentation.problem

import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.View
import com.makecity.client.R
import com.makecity.client.app.AppInjector
import com.makecity.client.presentation.lists.TaskDetailAdapter
import com.makecity.core.extenstion.calculateDiffs
import com.makecity.core.extenstion.isVisible
import com.makecity.core.extenstion.withArguments
import com.makecity.core.presentation.screen.ToolbarConfig
import com.makecity.core.presentation.screen.ToolbarScreen
import com.makecity.core.presentation.state.PrimaryViewState
import com.makecity.core.presentation.view.StatementFragment
import com.makecity.core.utils.Symbols.EMPTY
import com.makecity.core.utils.image.ImageManager
import kotlinx.android.synthetic.main.fragment_problem.*
import javax.inject.Inject

typealias ProblemStatement = StatementFragment<ProblemReducer, ProblemViewState, ProblemAction>


class ProblemFragment : ProblemStatement(), ToolbarScreen {

	companion object {
		private const val ARGUMENT_PROBLEM_DATA = "ARGUMENT_PROBLEM_DATA"
		fun newInstance(problemData: ProblemData) = ProblemFragment().withArguments {
			putParcelable(ARGUMENT_PROBLEM_DATA, problemData)
		}
	}

	@Inject
	lateinit var imageManager: ImageManager
	private lateinit var adapter: TaskDetailAdapter

	override val layoutId: Int = R.layout.fragment_problem

	override fun onInject() = AppInjector.inject(this, getArgument(ARGUMENT_PROBLEM_DATA))

	override fun getToolbar(): Toolbar = toolbar

	override fun onViewCreatedBeforeRender(savedInstanceState: Bundle?) {
		setupToolbarWith(requireActivity(), ToolbarConfig(
			title = EMPTY,
			isDisplayHomeButton = true
		))

		problem_refresh.setOnRefreshListener {
			reducer.reduce(LoadProblemAction)
		}

		problem_recycler.layoutManager = LinearLayoutManager(context)

		adapter = TaskDetailAdapter(imageManager, {

		}, {

		})

		//problem_recycler.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
		problem_recycler.adapter = adapter
		ViewCompat.setElevation(problem_message_group, 100f)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		trySetupContentSize(true)
		reducer.reduce(LoadProblemAction)
	}

	override fun onDestroyView() {
		super.onDestroyView()
		trySetupContentSize(false)
	}

	override fun render(state: ProblemViewState) = when (state.screenState) {
		is PrimaryViewState.Data -> {
			if (state.problemDetail != null) {
				adapter.calculateDiffs(state.problemDetail)
			}
			problem_progress.isVisible = false
			problem_recycler.isVisible = true
			problem_refresh.isRefreshing = false
		}
		is PrimaryViewState.Loading -> {
			problem_progress.isVisible = true
			problem_recycler.isVisible = false
			problem_refresh.isRefreshing = false
		}
		is PrimaryViewState.Error -> {

		}
	}
}