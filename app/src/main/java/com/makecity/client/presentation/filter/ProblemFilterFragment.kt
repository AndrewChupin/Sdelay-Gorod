package com.makecity.client.presentation.filter

import android.os.Bundle
import android.support.v7.widget.Toolbar
import com.makecity.client.R
import com.makecity.client.app.AppInjector
import com.makecity.core.presentation.screen.ToolbarScreen
import com.makecity.core.presentation.view.StatementFragment
import kotlinx.android.synthetic.main.toolbar.*


typealias ProblemFilterStatement = StatementFragment<ProblemFilterReducer, ProblemFilterViewState, ProblemFilterAction>


class ProblemFilterFragment : ProblemFilterStatement(), ToolbarScreen {

	companion object {
		fun newInstance() = ProblemFilterFragment()
	}

	override val layoutId: Int = R.layout.fragment_filter_problem

	override fun onInject() = AppInjector.inject(this)

	override fun getToolbar(): Toolbar = toolbar

	override fun onViewCreatedBeforeRender(savedInstanceState: Bundle?) {

	}

	override fun render(state: ProblemFilterViewState) {

	}
}