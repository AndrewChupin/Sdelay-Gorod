package com.makecity.client.data.geo

import com.makecity.core.domain.Mapper
import com.makecity.core.utils.Symbols.EMPTY


class GeoPointRemoteToPersist: Mapper<GeoPointRemote, GeoPointPersistence> {

	override fun transform(entity: GeoPointRemote): GeoPointPersistence = entity.run {
		GeoPointPersistence(
			cityId = cityId,
			countryId = countryId,
			regionId = regionId,
			important = important ?: false,
			region = region ?: EMPTY,
			area = area ?: EMPTY,
			title = title ?: EMPTY,
			latitude = latitude,
			longitude = longitude
		)
	}
}


class GeoPointPersistToCommon: Mapper<GeoPointPersistence, GeoPoint> {

	override fun transform(entity: GeoPointPersistence): GeoPoint = entity.run {
		GeoPoint(
			cityId = cityId,
			countryId = countryId,
			regionId = regionId,
			important = important,
			region = region,
			area = area,
			title = title,
			latitude = latitude,
			longitude = longitude
		)
	}
}
