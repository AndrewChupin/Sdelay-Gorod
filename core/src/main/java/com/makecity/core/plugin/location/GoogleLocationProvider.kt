package com.makecity.core.plugin.location

import android.content.Context
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.makecity.core.data.entity.Location
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject


class GoogleLocationProvider @Inject constructor(
	context: Context
): LocationProvider {

	companion object {
		private const val UPDATE_INTERVAL = 10L * 1000L
		private const val FASTEST_INTERVAL = 2000L
	}

	private val client = LocationServices.getFusedLocationProviderClient(context)
	private val request: LocationRequest = LocationRequest.create()

	init {
		request.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
		request.interval = UPDATE_INTERVAL
		request.fastestInterval = FASTEST_INTERVAL
		val builder = LocationSettingsRequest.Builder()
		builder.addLocationRequest(request)
		val locationSettingsRequest = builder.build()
		val settingsClient = LocationServices.getSettingsClient(context)
		settingsClient.checkLocationSettings(locationSettingsRequest)
	}

	override fun observeLocationUpdates(): Flowable<Location> {
		return RxFusedLocationProviderFlowable.create(client, request)
			.map { Location(it.latitude, it.longitude) }
	}
	override fun getLocation(): Single<Location> = FusedLocationProviderSingle.create(client)
		.map { Location(it.latitude, it.longitude) }

}
