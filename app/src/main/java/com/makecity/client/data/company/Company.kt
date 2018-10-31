package com.makecity.client.data.company

import com.makecity.core.data.Common
import com.makecity.core.data.Dto
import com.squareup.moshi.Json


@Dto
data class CompanyRemote(
	@Json(name = "org_id") val id: Long,
	@Json(name = "city_id") val cityId: Long,
	@Json(name = "name") val name: String?,
	@Json(name = "area") val area: String?,
	@Json(name = "logo") val logo: String?,
	@Json(name = "phone") val phone: String?
)


@Common
data class Company(
	val id: Long,
	val cityId: Long,
	val name: String,
	val area: String,
	val logo: String,
	val phone: String
)