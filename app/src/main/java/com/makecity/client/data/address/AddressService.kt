package com.makecity.client.data.address

import com.makecity.client.data.common.GoogleApi
import com.makecity.core.data.entity.Location
import io.reactivex.Single
import javax.inject.Inject


data class AddressRequest(
	val location: Location,
	val key: String // TODO to header in Retrofit initialization
)


interface AddressService {
	fun loadAddress(request: AddressRequest): Single<AddressRemote>
}


class AddressServiceRetrofit @Inject constructor (
	private val geoApi: GoogleApi
) : AddressService {

	override fun loadAddress(request: AddressRequest): Single<AddressRemote> = geoApi
		.getAddressBy(request.location.run { "$latitude,$longitude" }, request.key)
		.map { response ->

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