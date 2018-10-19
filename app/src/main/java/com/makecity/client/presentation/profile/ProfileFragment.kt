package com.makecity.client.presentation.profile

import android.os.Bundle
import android.support.v7.widget.Toolbar
import com.makecity.client.R
import com.makecity.client.app.AppInjector
import com.makecity.core.presentation.screen.ToolbarConfig
import com.makecity.core.presentation.screen.ToolbarScreen
import com.makecity.core.presentation.view.StatementFragment
import kotlinx.android.synthetic.main.toolbar.*

typealias ProfileStatement = StatementFragment<ProfileReducer, ProfileViewState, ProfileAction>


class ProfileFragment : ProfileStatement(), ToolbarScreen {

	companion object {
		fun newInstance() = ProfileFragment()
	}

	override val layoutId: Int = R.layout.fragment_profile

	override fun onInject() = AppInjector.inject(this)

	override fun getToolbar(): Toolbar = toolbar

	override fun onViewCreatedBeforeRender(savedInstanceState: Bundle?) {
		setupToolbarWith(requireActivity(), ToolbarConfig(
			title = getString(R.string.profile),
			isDisplayHomeButton = true
		))
	}

	override fun render(state: ProfileViewState) {

	}
}