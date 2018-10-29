package com.makecity.client.domain.auth

import com.makecity.core.domain.Validator
import javax.inject.Inject


data class CreatePasswordValidationRequest(
	val password: String,
	val repeatedPassword: String
)

class CreatePasswordValidator @Inject constructor() : Validator<CreatePasswordValidationRequest, AuthValidationResponse> {
	override fun validate(request: CreatePasswordValidationRequest): AuthValidationResponse {
		if (request.password == request.repeatedPassword) {
			return AuthValidationResponse(true)
		}

		return AuthValidationResponse(false)
	}
}