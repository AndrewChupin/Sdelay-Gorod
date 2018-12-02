package com.makecity.client.domain.auth

import com.makecity.client.data.auth.AuthDataSource
import com.makecity.client.data.auth.AuthType
import com.makecity.client.data.auth.NextAuthStep
import com.makecity.client.data.geo.GeoDataSource
import com.makecity.core.domain.Result
import com.makecity.core.domain.Validator
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject


interface AuthInteractor {
	fun validateData(content: String, authType: AuthType): Single<Result<Boolean>>
	fun validateEquality(password: String, repeatedPassword: String): Single<Result<Boolean>>


	fun sendPhone(phone: String): Single<NextAuthStep>
	fun checkSms(code: String): Single<NextAuthStep>
	fun createPassword(password: String): Completable
	fun checkPassword(password: String): Completable
	fun getPhone(): Single<String>
	fun refreshSms(): Single<Boolean>
}


class AuthInteractorDefault @Inject constructor(
	private val authDataSource: AuthDataSource,
	private val geoDataSource: GeoDataSource,
	private val validatorContent: Validator<AuthValidationRequest, AuthValidationResponse>,
	private val validatorEquality: Validator<CreatePasswordValidationRequest, AuthValidationResponse>
) : AuthInteractor {

	override fun validateData(content: String, authType: AuthType): Single<Result<Boolean>> = Single.fromCallable {
		val result = validatorContent.validate(AuthValidationRequest(content, authType))

		if (result.isValid) {
			Result(true)
		} else {
			Result(false, result.error)
		}
	}

	override fun validateEquality(password: String, repeatedPassword: String): Single<Result<Boolean>> = Single.fromCallable {
		val result = validatorEquality.validate(CreatePasswordValidationRequest(
			password = password,
			repeatedPassword = repeatedPassword
		))

		if (result.isValid) {
			Result(true)
		} else {
			Result(false, result.error)
		}
	}

	override fun sendPhone(phone: String): Single<NextAuthStep> = Single.defer {
		geoDataSource
			.getDefaultGeoPoint()
			.flatMapSingle {
				authDataSource.getCode(phone, it.cityId)
			}
	}

	override fun checkSms(code: String): Single<NextAuthStep> = Single.defer {
		authDataSource.saveCode(code)
	}

	override fun createPassword(password: String): Completable = Completable.defer {
		authDataSource.savePassword(password)
	}

	override fun checkPassword(password: String): Completable = Completable.defer {
		authDataSource.checkPassword(password)
	}

	override fun getPhone(): Single<String> = Single.defer {
		authDataSource.findPhone()
	}

	override fun refreshSms(): Single<Boolean> = Single.defer {
		authDataSource.refreshSms()
	}
}