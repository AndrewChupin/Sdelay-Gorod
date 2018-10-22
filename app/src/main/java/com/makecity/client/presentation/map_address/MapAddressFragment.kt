package com.makecity.client.presentation.map_address

import android.os.Bundle
import android.support.v7.widget.Toolbar
import com.makecity.client.R
import com.makecity.client.app.AppInjector
import com.makecity.core.extenstion.hideWithScale
import com.makecity.core.extenstion.showWithScale
import com.makecity.core.plugin.connection.ConnectionState
import com.makecity.core.plugin.location.LocationState
import com.makecity.core.presentation.screen.ToolbarScreen
import com.makecity.core.presentation.state.PrimaryViewState
import com.makecity.core.presentation.view.MapStatementFragment
import com.makecity.core.presentation.view.map.BaseMapView
import com.makecity.core.presentation.view.map.CameraState
import com.makecity.core.presentation.view.map.StartMoving
import com.makecity.core.presentation.view.map.StopMoving
import com.makecity.core.utils.Symbols.EMPTY
import kotlinx.android.synthetic.main.fragment_map_adress.*
import kotlinx.android.synthetic.main.toolbar.*


typealias MapAddressStatement = MapStatementFragment<MapAddressReducer, MapAddressViewState, MapAddressAction>


class MapAddressFragment : MapAddressStatement(), ToolbarScreen {

	companion object {
		fun newInstance() = MapAddressFragment()
	}

	override val layoutId: Int = R.layout.fragment_map_adress

	override fun onInject() = AppInjector.inject(this)

	override fun getToolbar(): Toolbar = toolbar

	override fun setupMapView(): BaseMapView = map_addres_view

	override fun onViewCreatedBeforeRender(savedInstanceState: Bundle?) {
		fab_zoom_in.setOnClickListener { map_addres_view.zoomIn() }

		fab_zoom_out.setOnClickListener { map_addres_view.zoomOut() }

		fab_my_position.setOnClickListener { reducer.reduce(MapAddressAction.FindOwnLocation) }

		map_addres_view.cameraStateListener = ::cameraStateChanged
	}

	override fun render(state: MapAddressViewState) {
		when (state.connectionState) {
			is ConnectionState.Connect -> text_map_address_connection.hideWithScale()
			is ConnectionState.Disconnect -> text_map_address_connection.showWithScale()
		}

		when (state.locationState) {
			is LocationState.Founded ->
				map_addres_view.setCamera(state.locationState.location, withZoom = 17f)
		}

		when (state.screenState) {
			is PrimaryViewState.Data -> changeViewState(
				title = state.address?.name ?: EMPTY,
				description = getString(R.string.map_specify),
				isVisibleButton = true
			)
		}
	}

	private fun cameraStateChanged(state: CameraState) = when (state) {
		is StartMoving -> {
			changeViewState(
				title = getString(R.string.map_define),
				description = getString(R.string.map_choose_place)
			)
		}
		is StopMoving -> if (state.zoom < 11) {
			changeViewState(
				title = getString(R.string.map_too_far),
				description = getString(R.string.map_specify)
			)
		} else {
			changeViewState(
				title = getString(R.string.map_loading),
				description = getString(R.string.map_specify)
			)
			reducer.reduce(MapAddressAction.GetPoints(locationCenter = state.centerLocation))
		}
		else -> Unit
	}

	private fun changeViewState(title: String? = null, description: String? = null, isVisibleButton: Boolean = false) {
		if (isVisibleButton) button_next.showWithScale() else button_next.hideWithScale()
		text_map_address_status.text = title
		text_map_address_description.text = description
	}
}