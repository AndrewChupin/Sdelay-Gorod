package com.makecity.client.presentation.description

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View
import com.makecity.client.R
import com.makecity.client.app.AppInjector
import com.makecity.core.extenstion.withArguments
import com.makecity.core.presentation.screen.KeyboardScreen
import com.makecity.core.presentation.screen.ToolbarConfig
import com.makecity.core.presentation.screen.ToolbarScreen
import com.makecity.core.presentation.state.PrimaryViewState
import com.makecity.core.presentation.view.StatementFragment
import com.makecity.core.utils.Symbols.EMPTY
import kotlinx.android.synthetic.main.fragment_description.*

typealias DescriptionStatement = StatementFragment<DescriptionReducer, DescriptionViewState, DescriptionAction>


class DescriptionFragment : DescriptionStatement(), ToolbarScreen, KeyboardScreen {

	companion object {
		private const val ARGUMENT_DESCRIPTION_DATA = "ARGUMENT_DESCRIPTION_DATA"

		fun newInstance(
			descriptionScreenData: DescriptionScreenData
		) = DescriptionFragment().withArguments {
				putParcelable(ARGUMENT_DESCRIPTION_DATA, descriptionScreenData)
			}
	}

	override val layoutId: Int = R.layout.fragment_description

	override fun onInject() = AppInjector.inject(this, getArgument(ARGUMENT_DESCRIPTION_DATA))

	override fun getToolbar(): Toolbar = toolbar

	override fun onViewCreatedBeforeRender(savedInstanceState: Bundle?) {
		setupToolbarWith(requireActivity(), ToolbarConfig(
			title = EMPTY,
			isDisplayHomeButton = true
		))

		description_done clickReduce DescriptionAction.DescriptionComplete(description_content.text.toString())
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		reducer.reduce(DescriptionAction.CheckData)
		description_content.requestFocus()
		showKeyboard()
	}

	override fun onDestroyView() {
		super.onDestroyView()
		hideKeyboard()
	}

	override fun render(state: DescriptionViewState) {
		if (state.screenState == PrimaryViewState.Data) {
			description_content.setText(state.description)
			description_content.setSelection(description_content.text.length)
		}
	}
}