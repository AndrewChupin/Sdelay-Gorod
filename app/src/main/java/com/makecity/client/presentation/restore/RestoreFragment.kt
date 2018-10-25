package com.makecity.client.presentation.restore

import android.os.Bundle
import android.support.v7.widget.Toolbar
import com.makecity.client.R
import com.makecity.client.app.AppInjector
import com.makecity.core.presentation.screen.ToolbarConfig
import com.makecity.core.presentation.screen.ToolbarScreen
import com.makecity.core.presentation.view.StatementFragment
import kotlinx.android.synthetic.main.fragment_restore.*

typealias RestoreStatement = StatementFragment<RestoreReducer, RestoreViewState, RestoreAction>


class RestoreFragment : RestoreStatement(), ToolbarScreen {

	companion object {
		fun newInstance() = RestoreFragment()
	}

	override val layoutId: Int = R.layout.fragment_restore

	override fun onInject() = AppInjector.inject(this)

	override fun getToolbar(): Toolbar = toolbar

	override fun onViewCreatedBeforeRender(savedInstanceState: Bundle?) {
		setupToolbarWith(requireActivity(), ToolbarConfig(
			title = getString(R.string.restore_problem),
			isDisplayHomeButton = true
		))

		restore_allow_button clickReduce RestoreAction.RestoreAllow
		restore_deny_button clickReduce RestoreAction.RestoreDeny
	}

	override fun render(state: RestoreViewState) {}
}