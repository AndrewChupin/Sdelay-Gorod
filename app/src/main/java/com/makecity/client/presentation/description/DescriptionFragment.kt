package com.makecity.client.presentation.description

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View
import com.makecity.client.R
import com.makecity.client.app.AppInjector
import com.makecity.core.presentation.screen.KeyboardScreen
import com.makecity.core.presentation.screen.ToolbarConfig
import com.makecity.core.presentation.screen.ToolbarScreen
import com.makecity.core.presentation.view.StatementFragment
import com.makecity.core.utils.Symbols.EMPTY
import kotlinx.android.synthetic.main.fragment_description.*

typealias DescriptionStatement = StatementFragment<DescriptionReducer, DescriptionViewState, DescriptionAction>


class DescriptionFragment : DescriptionStatement(), ToolbarScreen, KeyboardScreen {

	companion object {
		fun newInstance() = DescriptionFragment()
	}

	override val layoutId: Int = R.layout.fragment_description

	override fun onInject() = AppInjector.inject(this)

	override fun getToolbar(): Toolbar = toolbar

	override fun onViewCreatedBeforeRender(savedInstanceState: Bundle?) {
		setupToolbarWith(requireActivity(), ToolbarConfig(
			title = EMPTY,
			isDisplayHomeButton = true
		))

		description_done.setOnClickListener {
			reducer.reduce(DescriptionAction.ShowMapAddress)
		}
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		description_content.requestFocus()
		showKeyboard()
	}

	override fun onDestroyView() {
		super.onDestroyView()
		hideKeyboard()
	}

	override fun render(state: DescriptionViewState) {
	}
}