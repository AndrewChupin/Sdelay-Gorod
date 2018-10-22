package com.makecity.client.data.auth

import com.makecity.core.domain.Mapper
import com.makecity.core.extenstion.blockingCompletable
import io.reactivex.Single
import javax.inject.Inject


data class NextAuthStep(
	val nextStep: AuthType
)


interface AuthDataSource {
	fun getCode(phone: String, cityId: Long): Single<NextAuthStep>

	fun saveCode(code: String): Single<NextAuthStep>

	fun savePassword(pass: String): Single<String>

	fun getToken(): Single<String>
}


class AuthDataSourceDefault @Inject constructor(
	private val authService: AuthService,
	private val authStorage: AuthStorage,
	private val authStepMapper: Mapper<String, NextAuthStep>
) : AuthDataSource {

	override fun getCode(phone: String, cityId: Long): Single<NextAuthStep> = authService
		.sendPhone(cityId, phone)
		.map(authStepMapper::transform)

	override fun saveCode(code: String): Single<NextAuthStep> = authStorage
		.getToken()
		.flatMap {
			if (it.isEmpty()) Single.error<Throwable>(TokenNotFounded)
			authService.sendCode(it, code)
		}
		.blockingCompletable(authStorage::setToken)
		.map(authStepMapper::transform)

	override fun savePassword(pass: String) = authService.sendPassword("", pass)

	override fun getToken(): Single<String> = authStorage.getToken()
}