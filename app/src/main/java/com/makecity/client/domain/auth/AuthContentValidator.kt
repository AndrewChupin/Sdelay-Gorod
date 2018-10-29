package com.makecity.client.domain.auth

import com.makecity.client.app.AppConst
import com.makecity.client.data.auth.AuthType
import com.makecity.core.domain.Validator
import javax.inject.Inject


data class AuthValidationRequest(
	val content: CharSequence,
	val authType: AuthType
)


class AuthContentValidator @Inject constructor() : Validator<AuthValidationRequest, AuthValidationResponse> {
	override fun validate(requests: AuthValidationRequest): AuthValidationResponse {
		when (requests.authType) {
			AuthType.SMS -> if (requests.content.length >= AppConst.SMS_CODE_LENGTH) {
				return AuthValidationResponse(true)
			}
			AuthType.PHONE -> if (requests.content.length >= AppConst.PHONE_LENGTH) {
				return AuthValidationResponse(true)
			}
			AuthType.PASSWORD -> if (requests.content.length > 3) {
				return AuthValidationResponse(true)
			}
			AuthType.CREATE_PASSWORD -> if (requests.content.length > 3) {
				return AuthValidationResponse(true)
			}
		}
		return AuthValidationResponse(false)
	}
}