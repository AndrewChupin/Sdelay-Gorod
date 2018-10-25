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
		fun newInstance() = CreateProblemFragment()
	}

	@Inject
	lateinit var imageManager: ImageManager
	private lateinit var adapter: ProblemPreviewAdapter

	override val layoutId: Int = R.layout.fragment_create_problem

	override fun onInject() = AppInjector.inject(this)

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
		reducer.reduce(CreateProblemAction.ApproveProblem)
	}

	override fun onChangeData(dataType: ProblemPreviewDataType) {
		reducer.reduce(CreateProblemAction.ChangePreviewData(dataType))
	}

	override fun render(state: CreateProblemViewState) {
		if (state.screenState == PrimaryViewState.Data) {
			state.tempProblem?.let {
				adapter.calculateDiffs(it)
			}
		}
	}
}