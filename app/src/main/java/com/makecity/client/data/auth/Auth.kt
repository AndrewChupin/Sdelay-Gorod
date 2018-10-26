package com.makecity.client.data.auth

import com.makecity.core.data.Common
import com.makecity.core.data.Dto
import com.squareup.moshi.Json

// Response
@Dto
data class NextStepResponse(
	@Json(name = "next_step") val nextStep: String? = null
)

@Dto
data class RegistrationTokenResponse(
	@Json(name = "token") val token: String? = null,
	@Json(name = "next_step") val nextStep: String? = null
)


@Dto
data class AuthTokenResponse(
	@Json(name = "token") val token: String? = null,
	@Json(name = "expired") val expired: String? = null,
	@Json(name = "username") val username: String? = null,
	@Json(name = "role") val role: String? = null,
	@Json(name = "profile_id") val profileId: Long

)

@Common
data class RegistrationTokenResult(
	val token: String,
	val nextStep: String
)

@Common
enum class AuthType {
	PHONE, SMS, PASSWORD, CREATE_PASSWORD
}