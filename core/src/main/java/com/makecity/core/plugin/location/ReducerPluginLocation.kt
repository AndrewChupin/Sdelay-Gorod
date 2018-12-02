package com.makecity.core.plugin.location

import android.Manifest
import com.makecity.core.data.entity.Location
import com.makecity.core.utils.NoLastKnownLocation
import com.makecity.core.utils.ReactiveActions
import com.makecity.core.utils.permission.PermissionManager
import com.makecity.core.utils.permission.PermissionState
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers


data class LocationPluginConfig(
	val defaultLocation: Location? = null
)


interface ReducerPluginLocation : ReactiveActions {

	val permissionManager: PermissionManager
	val locationProvider: LocationProvider
	val locationPluginConfig: LocationPluginConfig

	fun startObserveLocation() {
		permissionManager.requestPermission(
			Manifest.permission.ACCESS_COARSE_LOCATION,
			Manifest.permission.ACCESS_FINE_LOCATION)
			.bindSubscribe(
				scheduler = AndroidSchedulers.mainThread(),
				onNext = {
					when {
						it.granted -> {
							onLocationPermissionStateChanged(PermissionState.Granted(it.name))
							findMyLocation()
						}
						it.shouldShowRequestPermissionRationale -> onLocationPermissionStateChanged(PermissionState.Rationale(it.name))
						else -> onLocationPermissionStateChanged(PermissionState.Unknown(it.name))
					}
				},
				onError = {
					it.printStackTrace()
					onLocationPermissionStateChanged(PermissionState.Failure(it))
				}
			)
	}

	private fun findMyLocation() = locationProvider.getLocation()
		.onErrorResumeNext {
			if (it is NoLastKnownLocation)
				locationProvider.observeLocationUpdates().firstOrError()
			else
				Single.error(it)
		}.bindSubscribe(
			scheduler = AndroidSchedulers.mainThread(),
			onSuccess = { onLocationChanged(LocationState.Founded(it)) },
			onError = { error ->
				error.printStackTrace()
				locationPluginConfig.defaultLocation?.let {
					findDefaultLocation(it)
				} ?: run {
					onLocationChanged(LocationState.Failure(error))
				}
			}
		)

	private fun findDefaultLocation(location: Location) = Single.fromCallable { location }
		.bindSubscribe(
			scheduler = AndroidSchedulers.mainThread(),
			onSuccess = { onLocationChanged(LocationState.Founded(it)) },
			onError = { onLocationChanged(LocationState.Failure(it)) }
		)

	fun onLocationPermissionStateChanged(permissionState: PermissionState)

	fun onLocationChanged(locationState: LocationState)
}
