package com.makecity.client.data.address

import com.makecity.core.domain.Mapper
import javax.inject.Inject


class AddressMapperRemoteToCommon @Inject constructor() : Mapper<AddressRemote, Address> {

	override fun transform(entity: AddressRemote): Address = entity.run {
		Address(
			homeNumber = entity.homeNumber,
			street = street,
			location = location
		)
	}
}