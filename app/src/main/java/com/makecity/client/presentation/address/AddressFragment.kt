package com.makecity.client.presentation.address

import android.os.Bundle
import android.support.v7.widget.Toolbar
import com.makecity.client.R
import com.makecity.client.app.AppInjector
import com.makecity.core.presentation.screen.ToolbarScreen
import com.makecity.core.presentation.view.StatementFragment
import kotlinx.android.synthetic.main.toolbar.*


typealias AddressStatement = StatementFragment<AddressReducer, AddressViewState, AddressAction>


class AddressFragment : AddressStatement(), ToolbarScreen {

	companion object {
		fun newInstance() = AddressFragment()
	}

	override val layoutId: Int = R.layout.fragment_address

	override fun onInject() = AppInjector.inject(this)

	override fun getToolbar(): Toolbar = toolbar

	override fun onViewCreatedBeforeRender(savedInstanceState: Bundle?) {

	}

	override fun render(state: AddressViewState) {

	}
}