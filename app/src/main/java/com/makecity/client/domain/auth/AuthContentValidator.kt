package com.makecity.client.domain.auth

import com.makecity.client.app.AppConst
import com.makecity.client.data.auth.AuthType
import com.makecity.core.domain.Validator
import javax.inject.Inject


data class AuthValidationRequest(
	val content: CharSequence,
	val authType: AuthType
)


data class AuthValidationResponse(
	val isValid: Boolean = false,
	val error: Throwable? = null
)


class AuthContentValidator @Inject constructor() : Validator<AuthValidationRequest, AuthValidationResponse> {
	override fun validate(vararg requests: AuthValidationRequest): AuthValidationResponse {
		requests.forEach {
			when (it.authType) {
				AuthType.SMS -> if (it.content.length > AppConst.SMS_CODE_LENGTH) {
					return AuthValidationResponse(true)
				}
				AuthType.PHONE -> if (it.content.length > AppConst.PHONE_LENGTH) {
					return AuthValidationResponse(true)
				}
				AuthType.PASSWORD -> if (it.content.length > 3) {
					return AuthValidationResponse(true)
				}
				AuthType.CREATE_PASSWORD -> if (it.content.length > 3) {
					return AuthValidationResponse(true)
				}
				else -> Unit
			}
		}
		return AuthValidationResponse(false)
	}
}