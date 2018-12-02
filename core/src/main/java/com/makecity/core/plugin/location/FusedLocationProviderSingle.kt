package com.makecity.core.plugin.location

import android.annotation.SuppressLint
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.makecity.core.utils.NoLastKnownLocation
import io.reactivex.*
import io.reactivex.disposables.Disposables


class RxFusedLocationProviderFlowable private constructor(
	private val client: FusedLocationProviderClient,
	private val request: LocationRequest
) : FlowableOnSubscribe<Location> {

	companion object {
		fun create(client: FusedLocationProviderClient, request: LocationRequest): Flowable<Location> {
			return Flowable.defer { Flowable.create(RxFusedLocationProviderFlowable(client, request), BackpressureStrategy.BUFFER) }
		}
	}

	@SuppressLint("MissingPermission")
	override fun subscribe(emitter: FlowableEmitter<Location>) {
		val callback = Callback(emitter)

		emitter.setDisposable(Disposables.fromRunnable {
			client.removeLocationUpdates(callback)
		})

		client.requestLocationUpdates(request, callback, null)
		client.flushLocations()
	}

	private class Callback(val emitter: FlowableEmitter<Location>) : LocationCallback() {
		override fun onLocationResult(result: LocationResult) {
			if (result.lastLocation != null) {
				emitter.onNext(result.lastLocation)
			}
		}
	}
}


class FusedLocationProviderSingle private constructor(
	private val client: FusedLocationProviderClient
) : SingleOnSubscribe<Location> {

	companion object {
		fun create(client: FusedLocationProviderClient): Single<Location> {
			return Single.defer { Single.create(FusedLocationProviderSingle(client)) }
		}
	}

	@SuppressLint("MissingPermission")
	override fun subscribe(emitter: SingleEmitter<Location>) {
		client.lastLocation.addOnFailureListener {
			emitter.onError(it)
		}.addOnSuccessListener {
			if (it != null) {
				emitter.onSuccess(it)
			} else {
				emitter.onError(NoLastKnownLocation())
			}
		}
	}
}
