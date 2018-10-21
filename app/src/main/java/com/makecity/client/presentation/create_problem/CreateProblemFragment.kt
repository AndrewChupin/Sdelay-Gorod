package com.makecity.client.presentation.create_problem

import android.os.Bundle
import android.support.v7.widget.Toolbar
import com.makecity.client.R
import com.makecity.client.app.AppInjector
import com.makecity.core.presentation.screen.ToolbarScreen
import com.makecity.core.presentation.view.StatementFragment
import kotlinx.android.synthetic.main.toolbar.*


typealias CreateProblemStatement = StatementFragment<CreateProblemReducer, CreateProblemViewState, CreateProblemAction>


class CreateProblemFragment : CreateProblemStatement(), ToolbarScreen {

	companion object {
		fun newInstance() = CreateProblemFragment()
	}

	override val layoutId: Int = R.layout.fragment_create_problem

	override fun onInject() = AppInjector.inject(this)

	override fun getToolbar(): Toolbar = toolbar

	override fun onViewCreatedBeforeRender(savedInstanceState: Bundle?) {

	}

	override fun render(state: CreateProblemViewState) {

	}
}