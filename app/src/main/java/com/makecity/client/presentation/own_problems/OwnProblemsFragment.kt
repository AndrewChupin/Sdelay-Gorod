package com.makecity.client.presentation.own_problems

import android.os.Bundle
import android.support.v7.widget.Toolbar
import com.makecity.client.R
import com.makecity.client.app.AppInjector
import com.makecity.core.presentation.screen.ToolbarScreen
import com.makecity.core.presentation.view.StatementFragment
import kotlinx.android.synthetic.main.toolbar.*


typealias OwnProblemsStatement = StatementFragment<OwnProblemsReducer, OwnProblemsViewState, OwnProblemsAction>


class OwnProblemsFragment : OwnProblemsStatement(), ToolbarScreen {

	companion object {
		fun newInstance() = OwnProblemsFragment()
	}

	override val layoutId: Int = R.layout.fragment_own_problems

	override fun onInject() = AppInjector.inject(this)

	override fun getToolbar(): Toolbar = toolbar

	override fun onViewCreatedBeforeRender(savedInstanceState: Bundle?) {

	}

	override fun render(state: OwnProblemsViewState) {

	}
}