package com.makecity.client.presentation.map_address

import android.os.Bundle
import android.support.v7.widget.Toolbar
import com.makecity.client.R
import com.makecity.client.app.AppInjector
import com.makecity.core.presentation.screen.ToolbarScreen
import com.makecity.core.presentation.view.StatementFragment
import kotlinx.android.synthetic.main.toolbar.*


typealias MapAddressStatement = StatementFragment<MapAddressReducer, MapAddressViewState, MapAddressAction>


class MapAddressFragment : MapAddressStatement(), ToolbarScreen {

	companion object {
		fun newInstance() = MapAddressFragment()
	}

	override val layoutId: Int = R.layout.fragment_map_adress

	override fun onInject() = AppInjector.inject(this)

	override fun getToolbar(): Toolbar = toolbar

	override fun onViewCreatedBeforeRender(savedInstanceState: Bundle?) {

	}

	override fun render(state: MapAddressViewState) {

	}
}