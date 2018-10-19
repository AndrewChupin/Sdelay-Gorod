package com.makecity.core.extenstion

import com.google.android.gms.maps.model.LatLng


fun LatLng.distanceTo(latLng: LatLng): Float {
	val distance = FloatArray(2)
	android.location.Location.distanceBetween(latitude, longitude, latLng.latitude, latLng.longitude, distance)
	return distance[0]
}
