package com.makecity.client.presentation.create_problem

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.View
import com.makecity.client.R
import com.makecity.client.app.AppInjector
import com.makecity.client.data.temp_problem.TempProblem
import com.makecity.client.presentation.lists.ProblemPreviewAdapter
import com.makecity.client.presentation.lists.ProblemPreviewDelegate
import com.makecity.core.extenstion.calculateDiffs
import com.makecity.core.extenstion.isVisible
import com.makecity.core.extenstion.withArguments
import com.makecity.core.presentation.screen.ToolbarConfig
import com.makecity.core.presentation.screen.ToolbarScreen
import com.makecity.core.presentation.state.PrimaryViewState
import com.makecity.core.presentation.view.StatementFragment
import com.makecity.core.utils.Symbols.EMPTY
import com.makecity.core.utils.image.ImageManager
import kotlinx.android.synthetic.main.fragment_create_problem.*
import javax.inject.Inject


typealias CreateProblemStatement = StatementFragment<CreateProblemReducer, CreateProblemViewState, CreateProblemAction>


class CreateProblemFragment : CreateProblemStatement(), ToolbarScreen, ProblemPreviewDelegate {

	companion object {
		private const val ARGUMENT_CREATE_PROBLEM_DATA = "ARGUMENT_CREATE_PROBLEM_DATA"

		fun newInstance(data: CreateProblemData) = CreateProblemFragment()
			.withArguments {
				putParcelable(ARGUMENT_CREATE_PROBLEM_DATA, data)
			}
	}

	@Inject
	lateinit var imageManager: ImageManager
	private lateinit var adapter: ProblemPreviewAdapter

	override val layoutId: Int = R.layout.fragment_create_problem

	override fun onInject() = AppInjector.inject(this, getArgument(ARGUMENT_CREATE_PROBLEM_DATA))

	override fun getToolbar(): Toolbar = toolbar

	override fun onViewCreatedBeforeRender(savedInstanceState: Bundle?) {
		setupToolbarWith(requireActivity(), ToolbarConfig(
			title = EMPTY,
			isDisplayHomeButton = false
		))

		// Array of choices
		problem_preview_recycler.layoutManager = LinearLayoutManager(requireContext())
		adapter = ProblemPreviewAdapter(imageManager, this)

		problem_preview_recycler.adapter = adapter


	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		reducer.reduce(CreateProblemAction.LoadPreview)
	}

	override fun onApproveCreating(tempProblem: TempProblem) {
		reducer.reduce(CreateProblemAction.ApproveProblem(tempProblem))
	}

	override fun onChangeData(dataType: ProblemPreviewDataType) {
		reducer.reduce(CreateProblemAction.ChangePreviewData(dataType))
	}

	override fun render(state: CreateProblemViewState) {
		when (state.screenState) {
			is PrimaryViewState.Data -> {
				create_problem_coordinator.isVisible = true
				create_problem_loader.isVisible = false
				adapter.canEditInfo = state.canEdit
				state.tempProblem?.let {
					adapter.calculateDiffs(it)
				}
			}
			is PrimaryViewState.Loading -> {
				create_problem_coordinator.isVisible = false
				create_problem_loader.isVisible = true
			}
		}
	}
}