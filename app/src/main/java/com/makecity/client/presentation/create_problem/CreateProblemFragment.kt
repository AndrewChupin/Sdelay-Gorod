package com.makecity.client.presentation.create_problem

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import com.makecity.client.R
import com.makecity.client.app.AppInjector
import com.makecity.client.data.temp_problem.TempProblem
import com.makecity.client.presentation.lists.ProblemPreviewAdapter
import com.makecity.core.extenstion.calculateDiffs
import com.makecity.core.presentation.screen.ToolbarConfig
import com.makecity.core.presentation.screen.ToolbarScreen
import com.makecity.core.presentation.view.StatementFragment
import com.makecity.core.utils.Symbols.EMPTY
import com.makecity.core.utils.image.ImageManager
import kotlinx.android.synthetic.main.fragment_create_problem.*
import javax.inject.Inject


typealias CreateProblemStatement = StatementFragment<CreateProblemReducer, CreateProblemViewState, CreateProblemAction>


class CreateProblemFragment : CreateProblemStatement(), ToolbarScreen {

	companion object {
		fun newInstance() = CreateProblemFragment()
	}

	@Inject
	lateinit var imageManager: ImageManager

	override val layoutId: Int = R.layout.fragment_create_problem

	override fun onInject() = AppInjector.inject(this)

	override fun getToolbar(): Toolbar = toolbar

	override fun onViewCreatedBeforeRender(savedInstanceState: Bundle?) {
		setupToolbarWith(requireActivity(), ToolbarConfig(
			title = EMPTY,
			isDisplayHomeButton = true
		))

		// Array of choices
		problem_preview_recycler.layoutManager = LinearLayoutManager(requireContext())
		val adapter =  ProblemPreviewAdapter(imageManager) {

		}
		problem_preview_recycler.adapter = adapter
		adapter.calculateDiffs(TempProblem(
			0, 0, "Управляющая компания",
			"Не работает лифт", 0, "5bbf8b6a46b12.jpg", "",
			"Велосипедисты-энтузиасты построили на этом месте памп-трек - грунтовую велосипедную закольцованную трассу, представляющую собой чередование ям, кочек и контруклонов.",
			56.85694, 53.2215, "ул. Ленина, 84")
		)

	}

	override fun render(state: CreateProblemViewState) {

	}
}