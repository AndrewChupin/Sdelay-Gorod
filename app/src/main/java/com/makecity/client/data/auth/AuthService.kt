package com.makecity.client.data.auth

import com.makecity.client.data.common.Api
import com.squareup.moshi.Json
import io.reactivex.Single
import javax.inject.Inject


// Requests
data class GetSmsRequestBody(
	@Json(name = "phone") val phone: String,
	@Json(name = "cityId") val cityId: Long
)


data class NewSmsRequestBody(
	@Json(name = "phone") val phone: String
)

data class CheckSmsRequestBody(
	@Json(name = "phone") val phone: String,
	@Json(name = "code") val code: String
)

data class CreatePasswordRequestBody(
	@Json(name = "reg_token") val regToken: String,
	@Json(name = "pass") val pass: String
)

data class CheckPasswordRequestBody(
	@Json(name = "phoneOrEmail") val phone: String,
	@Json(name = "password") val password: String
)


interface AuthService {
	fun sendPhone(phone: String, cityId: Long): Single<NextStepResponse>

	fun getNewSms(phone: String): Single<Boolean>

	fun sendCode(phone: String, code: String): Single<RegistrationTokenResponse>

	fun setPassword(token: String, pass: String): Single<AuthTokenResponse>

	fun checkPassword(phone: String, pass: String): Single<AuthTokenResponse>
}


class AuthServiceDefault @Inject constructor(
	private val api: Api
) : AuthService {

	override fun getNewSms(phone: String): Single<Boolean> = api
		.refreshSms(NewSmsRequestBody(phone))

	override fun sendPhone(phone: String, cityId: Long): Single<NextStepResponse> = api
		.sendPhone(GetSmsRequestBody(phone, cityId))

	override fun sendCode(phone: String, code: String): Single<RegistrationTokenResponse> = api
		.confirmPhone(CheckSmsRequestBody(phone, code))

	override fun setPassword(token: String, pass: String): Single<AuthTokenResponse> = api
		.setPassword(CreatePasswordRequestBody(token, pass))

	override fun checkPassword(phone: String, pass: String): Single<AuthTokenResponse> = api
		.checkPassword(CheckPasswordRequestBody(phone, pass))
}