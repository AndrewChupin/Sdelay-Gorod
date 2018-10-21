package com.makecity.client.presentation.city

import android.os.Bundle
import android.support.v7.widget.Toolbar
import com.makecity.client.R
import com.makecity.client.app.AppInjector
import com.makecity.core.presentation.screen.ToolbarScreen
import com.makecity.core.presentation.view.StatementFragment
import kotlinx.android.synthetic.main.fragment_city.*
import kotlinx.android.synthetic.main.toolbar.*


typealias CityStatement = StatementFragment<CityReducer, CityViewState, CityAction>


class CityFragment : CityStatement(), ToolbarScreen {

	companion object {
		fun newInstance() = CityFragment()
	}

	override val layoutId: Int = R.layout.fragment_city

	override fun onInject() = AppInjector.inject(this)

	override fun getToolbar(): Toolbar = toolbar

	override fun onViewCreatedBeforeRender(savedInstanceState: Bundle?) {

	}

	override fun render(state: CityViewState) {

	}
}