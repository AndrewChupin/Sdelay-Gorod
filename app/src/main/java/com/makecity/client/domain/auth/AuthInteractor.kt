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
	fun sendPhone(phone: String): Single<NextAuthStep>
	fun checkSms(code: String): Single<NextAuthStep>
	fun createPassword(password: String): Completable
	fun checkPassword(password: String): Completable
}


class AuthInteractorDefault @Inject constructor (
	private val authDataSource: AuthDataSource,
	private val geoDataSource: GeoDataSource,
	private val validator: Validator<AuthValidationRequest, AuthValidationResponse>
) : AuthInteractor {

	override fun validateData(content: String, authType: AuthType): Single<Result<Boolean>> = Single.fromCallable {
		val result = validator.validate(AuthValidationRequest(content, authType))
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
}