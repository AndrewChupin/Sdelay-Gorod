package com.makecity.client.data.auth

import com.makecity.core.domain.Mapper
import com.makecity.core.extenstion.blockingCompletable
import com.makecity.core.utils.Symbols.EMPTY
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject


data class NextAuthStep(
	val nextStep: AuthType
)


interface AuthDataSource {
	fun getCode(phone: String, cityId: Long): Single<NextAuthStep>

	fun saveCode(code: String): Single<NextAuthStep>

	fun savePassword(pass: String): Completable

	fun checkPassword(pass: String): Completable

	fun getToken(): Single<String>

	fun findPhone(): Single<String>

	fun refreshSms(): Single<Boolean>
}


class AuthDataSourceDefault @Inject constructor(
	private val authService: AuthService,
	private val authStorage: AuthStorage,
	private val authStepMapper: Mapper<String, NextAuthStep>
) : AuthDataSource {

	override fun getCode(phone: String, cityId: Long): Single<NextAuthStep> = Single.defer {
		authService
			.sendPhone(phone, cityId)
			.map { it.nextStep ?: EMPTY }
			.map(authStepMapper::transform)
			.blockingCompletable { authStorage.setPhone(phone) }
	}

	override fun saveCode(code: String): Single<NextAuthStep> = Single.defer {
		authStorage
			.getPhone()
			.flatMap { authService.sendCode(it, code) }
			.map {
				RegistrationTokenResult(it.token ?: throw TokenNotFounded, it.nextStep ?: EMPTY)
			}
			.blockingCompletable { authStorage.setRegistrationToken(it.token) }
			.map(RegistrationTokenResult::nextStep)
			.map(authStepMapper::transform)
	}

	override fun savePassword(pass: String): Completable = Completable.defer {
		authStorage
			.getRegistrationToken()
			.flatMap { authService.setPassword(it, pass) }
			.flatMapCompletable {
				authStorage
					.setAuthToken(it.token  ?: throw TokenNotFounded)
					.andThen(authStorage.setRegistrationToken(EMPTY))
			}
			//.andThen { authStorage.setRegistrationToken(EMPTY) }
	}

	override fun checkPassword(pass: String): Completable = Completable.defer {
		authStorage
			.getPhone()
			.flatMap { authService.checkPassword(it, pass) }
			.flatMapCompletable { authStorage.setAuthToken(it.token ?: throw TokenNotFounded) }
	}

	override fun getToken(): Single<String> = authStorage.getRegistrationToken()

	override fun findPhone(): Single<String> = authStorage
		.getPhone()

	override fun refreshSms(): Single<Boolean> = authStorage
		.getPhone()
		.flatMap { authService.getNewSms(it) }
}