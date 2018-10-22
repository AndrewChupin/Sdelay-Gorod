package com.makecity.client.data.auth

import com.makecity.client.data.common.Api
import io.reactivex.Single
import javax.inject.Inject


interface AuthService {
	fun sendPhone(cityId: Long, phone: String): Single<String>

	fun sendCode(token: String, code: String): Single<String>

	fun sendPassword(token: String, pass: String): Single<String>
}


class AuthServiceDefault @Inject constructor(
	private val api: Api
) : AuthService {
	override fun sendPhone(cityId: Long, phone: String): Single<String> = api.sendPhone(cityId, phone)

	override fun sendCode(token: String, code: String): Single<String> = api.confirmPhone(token, code)

	override fun sendPassword(token: String, pass: String): Single<String> = api.setPassword(token, pass)
}