package com.makecity.client.data.address

import com.makecity.core.data.Dto
import com.makecity.core.data.Presentation
import com.makecity.core.data.entity.Location
import com.squareup.moshi.Json

@Dto
data class GeoCodeResponseRemote(
	@Json(name = "results") val results: List<GeoCodeResultRemote>
)

@Dto
data class GeoCodeResultRemote(
	@Json(name = "address_components") val addressComponents: List<AddressComponentRemote>,
	@Json(name = "geometry") val geometry: GeometryRemote
)

@Dto
data class GeometryRemote(
	@Json(name = "location") val location: LocationRemote
)

@Dto
data class LocationRemote(
	@Json(name = "lat") val lat: Double,
	@Json(name = "lng") val lon: Double
)

@Dto
data class AddressComponentRemote(
	@Json(name = "short_name") val name: String,
	@Json(name = "types") val types: List<String>
)


@Dto
data class AddressRemote(
	val homeNumber: String,
	val street: String,
	val location: Location
)

@Presentation
data class Address(
	val homeNumber: String,
	val street: String,
	val location: Location
)