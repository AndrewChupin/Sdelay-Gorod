package com.makecity.client.presentation.about

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.text.method.LinkMovementMethod
import android.view.View
import com.makecity.client.R
import com.makecity.client.app.AppInjector
import com.makecity.core.extenstion.textWithExecutableHref
import com.makecity.core.presentation.screen.ToolbarConfig
import com.makecity.core.presentation.screen.ToolbarScreen
import com.makecity.core.presentation.view.StatementFragment
import kotlinx.android.synthetic.main.fragment_about_project.*
import kotlinx.android.synthetic.main.toolbar.*


typealias AboutStatement = StatementFragment<AboutReducer, AboutViewState, AboutAction>


class AboutFragment : AboutStatement(), ToolbarScreen {

	companion object {
		fun newInstance() = AboutFragment()
	}

	override val layoutId: Int = R.layout.fragment_about_project

	override fun onInject() = AppInjector.inject(this)

	override fun getToolbar(): Toolbar = toolbar

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		setupToolbarWith(requireActivity(), ToolbarConfig(
			title = getString(R.string.about_project),
			isDisplayHomeButton = true
		))

		about_content.apply {
			movementMethod = LinkMovementMethod.getInstance()
			textWithExecutableHref(getString(R.string.about_content)) { _ ->
				reducer.reduce(ShowSupportScreen)
			}
		}
	}

	override fun onViewCreatedBeforeRender(savedInstanceState: Bundle?) {}

	override fun render(state: AboutViewState) {}
}