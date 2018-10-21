package com.makecity.client.presentation.settings

import android.os.Bundle
import android.support.v7.widget.Toolbar
import com.makecity.client.R
import com.makecity.client.app.AppInjector
import com.makecity.core.presentation.screen.ToolbarScreen
import com.makecity.core.presentation.view.StatementFragment
import kotlinx.android.synthetic.main.toolbar.*


typealias SettingsStatement = StatementFragment<SettingsReducer, SettingsViewState, SettingsAction>


class SettingsFragment : SettingsStatement(), ToolbarScreen {

	companion object {
		fun newInstance() = SettingsFragment()
	}

	override val layoutId: Int = R.layout.fragment_settings

	override fun onInject() = AppInjector.inject(this)

	override fun getToolbar(): Toolbar = toolbar

	override fun onViewCreatedBeforeRender(savedInstanceState: Bundle?) {

	}

	override fun render(state: SettingsViewState) {

	}
}