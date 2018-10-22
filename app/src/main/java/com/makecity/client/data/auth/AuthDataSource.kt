package com.makecity.client.data.auth

import com.makecity.core.domain.Mapper
import com.makecity.core.extenstion.blockingCompletable
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject


data class NextAuthStep(
	val nextStep: AuthType
)


interface AuthDataSource {
	fun getCode(phone: String): Single<NextAuthStep>

	fun saveCode(code: String): Single<NextAuthStep>

	fun savePassword(pass: String): Single<String>

	fun getToken(): Single<String>

	fun getCity(): Single<Long>

	fun saveCity(cityId: Long): Completable
}


class AuthDataSourseDefault @Inject constructor(
	private val authService: AuthService,
	private val authStorage: AuthStorage,
	private val authStepMapper: Mapper<String, NextAuthStep>
) : AuthDataSource {

	override fun getCode(phone: String): Single<NextAuthStep> = authStorage
		.getDefaultCity()
		.flatMap {
			if (it < 0) Single.error<Throwable>(CityIdNotFounded)
			authService.sendPhone(it, phone)
		}
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

	override fun getCity(): Single<Long> = authStorage.getDefaultCity()

	override fun saveCity(cityId: Long): Completable = authStorage.putDefaultCity(cityId)
}