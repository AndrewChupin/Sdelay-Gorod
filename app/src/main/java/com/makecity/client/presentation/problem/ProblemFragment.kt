package com.makecity.client.presentation.problem

import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import com.makecity.client.R
import com.makecity.client.app.AppInjector
import com.makecity.client.data.task.Task
import com.makecity.client.presentation.lists.TaskDetailAdapter
import com.makecity.client.presentation.lists.TaskDetailsDelegate
import com.makecity.client.utils.GoogleApiHelper
import com.makecity.core.data.entity.Location
import com.makecity.core.extenstion.calculateDiffs
import com.makecity.core.extenstion.isVisible
import com.makecity.core.extenstion.withArguments
import com.makecity.core.presentation.screen.KeyboardScreen
import com.makecity.core.presentation.screen.ToolbarConfig
import com.makecity.core.presentation.screen.ToolbarScreen
import com.makecity.core.presentation.state.PrimaryViewState
import com.makecity.core.presentation.view.StatementFragment
import com.makecity.core.utils.ScreenUtils
import com.makecity.core.utils.Symbols.EMPTY
import com.makecity.core.utils.image.CommonImageRules
import com.makecity.core.utils.image.ImageManager
import kotlinx.android.synthetic.main.fragment_auth.*
import kotlinx.android.synthetic.main.fragment_problem.*
import kotlinx.android.synthetic.main.item_problem_location.*
import javax.inject.Inject

typealias ProblemStatement = StatementFragment<ProblemReducer, ProblemViewState, ProblemAction>


class ProblemFragment : ProblemStatement(), ToolbarScreen, TaskDetailsDelegate, KeyboardScreen {

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

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		reducer.reduce(ProblemAction.LoadProblem)
	}

	override fun onViewCreatedBeforeRender(savedInstanceState: Bundle?) {
		setupToolbarWith(requireActivity(), ToolbarConfig(
			title = EMPTY,
			isDisplayHomeButton = true
		))

		problem_refresh.setOnRefreshListener {
			reducer.reduce(ProblemAction.LoadProblem)
		}
		textView.setOnClickListener {
			reducer.reduce(ProblemAction.CreateComment(et_chat_message_input.text.toString()))
			et_chat_message_input.setText(EMPTY)
		}
		problem_send_comment_button.setOnClickListener {
			reducer.reduce(ProblemAction.CreateComment(et_chat_message_input.text.toString()))
			et_chat_message_input.setText(EMPTY)
		}

		problem_recycler.layoutManager = LinearLayoutManager(context)

		adapter = TaskDetailAdapter(imageManager, {

		}, {

		}, this)

		//problem_recycler.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
		problem_recycler.adapter = adapter
		ViewCompat.setElevation(problem_message_group, 100f)
	}


	override fun onDestroyView() {
		super.onDestroyView()
		hideKeyboard()
	}

	override fun render(state: ProblemViewState) {
		when (state.screenState) {
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

	override fun showMoreCommentsClicked() = reducer.reduce(ProblemAction.ShowMoreComments)


	override fun likeClicked(task: Task) = reducer.reduce(ProblemAction.ChangeFavorite(task))
}