package com.makecity.client.domain.auth

import com.makecity.client.data.auth.AuthDataSource
import com.makecity.client.data.auth.AuthType
import com.makecity.client.data.auth.NextAuthStep
import com.makecity.core.domain.Result
import com.makecity.core.domain.Validator
import io.reactivex.Single
import javax.inject.Inject


interface AuthInteractor {
	fun validateData(content: String, authType: AuthType): Single<Result<Boolean>>
	fun sendDataData(content: String, authType: AuthType): Single<NextAuthStep>
}


class AuthInteractorDefault @Inject constructor (
	private val authDataSource: AuthDataSource,
	private val validator: Validator<AuthValidationRequest, AuthValidationResponse>
) : AuthInteractor {

	override fun validateData(content: String, authType: AuthType): Single<Result<Boolean>> = Single.fromCallable {
		val result = validator.validate(AuthValidationRequest(content, authType))
		if (result.isValid) {
			Result(true)
		} else {
			result.error?.let {
				Result(false, it)
			}
		}
	}

	override fun sendDataData(content: String, authType: AuthType): Single<NextAuthStep> = when (authType) {
		AuthType.PHONE -> authDataSource.getCode(content)
		AuthType.SMS -> authDataSource.saveCode(content)
		else -> Single.error(IllegalStateException())
	}
}