package com.makecity.client.data.address

import com.makecity.client.data.common.GoogleApi
import com.makecity.core.data.entity.Location
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject


data class AddressRequest(
	val location: Location,
	val key: String // TODO LATE to header in Retrofit initialization
)


interface AddressService {
	fun locationUpdated(request: AddressRequest)
	fun observeAddress(): Observable<AddressRemote>
}


class AddressServiceRetrofit @Inject constructor(
	private val geoApi: GoogleApi
) : AddressService {

	private val subject: PublishSubject<AddressRequest> = PublishSubject.create()

	override fun locationUpdated(request: AddressRequest) {
		subject.onNext(request)
	}

	override fun observeAddress(): Observable<AddressRemote> {
		return subject
			.observeOn(Schedulers.io())
			.debounce(500, TimeUnit.MILLISECONDS)
			.switchMap {
				geoApi.getAddressBy(it.location.run { "$latitude,$longitude" }, it.key)
			}
			.map { response ->
				// TODO LATE MAPPER
				val location = response.results.first().geometry.location.run {
					Location(lat, lon)
				}

				val house = response.results
					.first()
					.addressComponents
					.first { it.types.contains("street_number") }
					.name

				val street = response.results
					.first()
					.addressComponents
					.first { it.types.contains("route") }
					.name

				AddressRemote(house, street, location)
			}
	}
}