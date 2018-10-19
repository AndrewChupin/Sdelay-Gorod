package com.makecity.core.extenstion

import com.google.android.gms.maps.model.LatLngBounds


val LatLngBounds.radius: Int
	get() = distance / 2


val LatLngBounds.distance: Int
	get() {
		val southWestLatLon = southwest
		val northEastLatLon = northeast
		return southWestLatLon.distanceTo(northEastLatLon).toInt()
	}
