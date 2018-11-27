package com.makecity.client.data.profile

import com.makecity.core.domain.Mapper
import com.makecity.core.utils.DataStructureException
import com.makecity.core.utils.Symbols.EMPTY
import javax.inject.Inject


class ProfileDtoToPersistMapper @Inject constructor() : Mapper<ProfileRemote, ProfilePersistence> {

	override fun transform(entity: ProfileRemote): ProfilePersistence = entity.run {
		ProfilePersistence(
			cityId = cityId ?: throw DataStructureException,
			firstName = firstName ?: EMPTY,
			lastName = lastName ?: EMPTY,
			street = street ?: EMPTY,
			sex = sex ?: EMPTY,
			house = house ?: EMPTY,
			phone = user.phone ?: EMPTY,
			photo = user.photo ?: EMPTY,
			defaultCompanyId = defaultCompanyId ?: -1L
		)
	}
}


class ProfilePersistToCommonMapper @Inject constructor() : Mapper<ProfilePersistence, Profile> {
	override fun transform(entity: ProfilePersistence): Profile = entity.run {
		Profile(
			id = id,
			firstName = firstName,
			lastName = lastName,
			photo = photo,
			phone = phone,
			sex = sex,
			defaultCompanyId = defaultCompanyId,
			cityId = cityId,
			street = street,
			house = house
		)
	}
}
