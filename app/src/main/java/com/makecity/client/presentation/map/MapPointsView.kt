package com.makecity.client.presentation.map

import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.makecity.client.R
import com.makecity.client.data.common.MapPoint
import com.makecity.client.data.task.Task
import com.makecity.core.data.entity.Location
import com.makecity.core.extenstion.addMarker
import com.makecity.core.presentation.view.map.BaseMapView
import android.content.res.Resources.NotFoundException
import com.google.android.gms.maps.model.MapStyleOptions




class MapPointsView: BaseMapView, GoogleMap.OnMarkerClickListener {

	constructor(context: Context) : super(context)

	constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

	constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

	// Markers
	private var markers: MutableList<Marker> = mutableListOf()

	var pointClickListener: ((MapPoint) -> Boolean)? = null

	init {
		getMapAsync(this)
	}

	override fun onMapReady(googleMap: GoogleMap) {
		super.onMapReady(googleMap)

		try {
			googleMap.setMapStyle(
				MapStyleOptions.loadRawResourceStyle(
					context, R.raw.uber_style)) // TODO CHECK SUCCESS

		} catch (e: Resources.NotFoundException) {
			e.printStackTrace()
		}


		showLocationOnMap()
		googleMap.setOnMarkerClickListener(this)
	}

	override fun onMarkerClick(marker: Marker?): Boolean {
		pointClickListener?.let { listener ->
			marker?.let {
				if (it.tag is MapPoint) {
					return listener(it.tag as MapPoint)
				}
			}
		}
		return false
	}

	fun updatePoints(points: List<Task>) {
		markers.filter { marker ->
			points.none { it == marker.tag }
		}.forEach {
			markers.remove(it)
			it.remove()
		}

		points.forEach { point ->
			markers.find {
				it.tag == it
			}?.let {
				it.position = LatLng(point.latitude, point.longitude)
			} ?: map?.run {
				val marker = this.addMarker(Location(point.latitude, point.longitude), R.drawable.pin, point)
				markers.add(marker)
			}
		}
	}
}