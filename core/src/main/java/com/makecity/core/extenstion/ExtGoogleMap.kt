package com.makecity.core.extenstion

import android.support.annotation.DrawableRes
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.makecity.core.data.entity.Location


fun GoogleMap.addMarker(location: Location, @DrawableRes resId: Int, tag: Any? = null): Marker {
	val marker = addMarker(MarkerOptions()
		.position(LatLng(location.latitude, location.longitude))
		.icon(BitmapDescriptorFactory.fromResource(resId))
		.rotation(0f)
	)
	marker.tag = tag
	return marker
}
