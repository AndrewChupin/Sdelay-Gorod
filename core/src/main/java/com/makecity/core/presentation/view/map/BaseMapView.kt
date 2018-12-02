package com.makecity.core.presentation.view.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.annotation.CallSuper
import android.util.AttributeSet
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.makecity.core.data.entity.Location
import com.makecity.core.extenstion.checkAnyPermission
import com.makecity.core.extenstion.radius


open class BaseMapView : MapView, OnMapReadyCallback {

	companion object {
		const val DEFAULT_ZOOM_LEVEL = 13.0f

		const val ARGUMENT_ZOOM_VALUE = "ARGUMENT_ZOOM_VALUE"
		const val ARGUMENT_POSITION_VALUE = "ARGUMENT_POSITION_VALUE"
	}

	constructor(context: Context) : super(context)

	constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

	constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

	// Google Map
	protected var map: GoogleMap? = null
	protected var savedPosition: CameraPosition? = null

	// Moving or Stop (Note: protected setter)
	var cameraState: CameraState = InitMoving
		protected set(value) {
			field = value
			cameraStateListener?.let {
				it(value)
			}
		}

	// Current position
	var mapState: MapState? = null
		get() {
			return map?.cameraPosition?.let {
				MapState(
					position = Position(
						latitude = it.target.latitude,
						longitude = it.target.longitude,
						zoom = it.zoom
					)
				)
			}
		}


	// Delegates
	var mapInteractionReady: (() -> Unit)? = null
	var cameraStateListener: ((CameraState) -> Unit)? = null

	@CallSuper
	override fun onMapReady(googleMap: GoogleMap) {
		map = googleMap

		savedPosition?.let {
			googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(it))
		}

		mapInteractionReady?.invoke()

		googleMap.setOnCameraIdleListener {
			val bounds = googleMap.projection.visibleRegion.latLngBounds
			cameraState = StopMoving(
				centerLocation = Location(
					latitude = googleMap.cameraPosition.target.latitude,
					longitude = googleMap.cameraPosition.target.longitude
				),
				locationNe = Location(
					latitude = bounds.northeast.latitude,
					longitude = bounds.northeast.longitude
				),
				locationSw = Location(
					latitude = bounds.southwest.latitude,
					longitude = bounds.southwest.longitude
				),
				zoom = googleMap.cameraPosition.zoom,
				radius = bounds.radius
			)
		}

		googleMap.setOnCameraMoveStartedListener { cameraState = StartMoving }
	}

	@SuppressLint("MissingPermission")
	protected fun showLocationOnMap() {
		map?.apply {
			context.checkAnyPermission(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION) {
				isMyLocationEnabled = true
				uiSettings.isMyLocationButtonEnabled = false
			}
		}
	}

	fun zoomIn() = map?.animateCamera(CameraUpdateFactory.zoomIn())

	fun zoomOut() = map?.animateCamera(CameraUpdateFactory.zoomOut())

	fun setCamera(location: Location, withZoom: Float = DEFAULT_ZOOM_LEVEL, withAnimation: Boolean = false) {
		if (withAnimation) {
			map?.animateCamera(
				CameraUpdateFactory.newLatLngZoom(
					LatLng(
						location.latitude,
						location.longitude
					),
					withZoom
				)
			)
		} else {
			map?.moveCamera(
				CameraUpdateFactory.newLatLngZoom(
					LatLng(
						location.latitude,
						location.longitude
					),
					withZoom
				)
			)
		}
	}

	fun storeCameraState(bundle: Bundle?) {
		try {
			val safeMap = map
			if (safeMap != null && bundle != null) {
				bundle.putFloat(ARGUMENT_ZOOM_VALUE, safeMap.cameraPosition.zoom)
				bundle.putParcelable(ARGUMENT_POSITION_VALUE, safeMap.cameraPosition.target)
				return
			}

			val savedPositionSafe = savedPosition
			if (savedPositionSafe != null && bundle != null) {
				bundle.putFloat(ARGUMENT_ZOOM_VALUE, savedPositionSafe.zoom)
				bundle.putParcelable(ARGUMENT_POSITION_VALUE, savedPositionSafe.target)
			}
		} finally {
			savedPosition = null
		}
	}

	fun restoreCameraState(bundle: Bundle?) {
		if (bundle != null && !bundle.isEmpty) {
			val latLon = bundle.getParcelable<LatLng>(ARGUMENT_POSITION_VALUE)
			val zoom = bundle.getFloat(ARGUMENT_ZOOM_VALUE)
			savedPosition = CameraPosition.fromLatLngZoom(latLon, zoom)
		}
	}
}
