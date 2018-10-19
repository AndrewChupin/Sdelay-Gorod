package com.makecity.client.data.common

import com.makecity.core.data.Common
import com.makecity.core.data.entity.Location


@Common
data class MapPoint(
	val partnerId: String,
	val externalId: String,
	val partnerName: String,
	val address: String,
	val imageUrl: String,
	val isVisited: Boolean,
	val location: Location
)
