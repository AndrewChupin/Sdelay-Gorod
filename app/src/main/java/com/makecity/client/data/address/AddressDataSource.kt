package com.makecity.client.data.address

import com.makecity.client.BuildConfig
import com.makecity.core.data.entity.Location
import com.makecity.core.domain.Mapper
import io.reactivex.Observable
import javax.inject.Inject


interface AddressDataSource {
	fun getAddress(location: Location)
	fun observeAddress(): Observable<Address>
}


class AddressDataSourceDefault @Inject constructor(
	private val addressService: AddressService,
	private val mapper: Mapper<AddressRemote, Address>
) : AddressDataSource {

	override fun getAddress(location: Location)
		= addressService.locationUpdated(AddressRequest(location, BuildConfig.GOOGLE_MAPS_KEY))

	override fun observeAddress(): Observable<Address> = addressService
		.observeAddress()
		.map(mapper::transform)
}