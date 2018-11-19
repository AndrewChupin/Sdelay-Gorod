package com.makecity.client.presentation.map_address

import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.MapStyleOptions
import com.makecity.client.R
import com.makecity.core.presentation.view.map.BaseMapView

class MapAddressMap: BaseMapView {

	constructor(context: Context) : super(context)

	constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

	constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

	init {
		getMapAsync(this)
	}

	override fun onMapReady(googleMap: GoogleMap) {
		super.onMapReady(googleMap)

		try {
			googleMap.setMapStyle(
				MapStyleOptions.loadRawResourceStyle(
					context, R.raw.sdelay_gorod_style))

		} catch (e: Resources.NotFoundException) {
			e.printStackTrace()
		}

		showLocationOnMap()
	}
}