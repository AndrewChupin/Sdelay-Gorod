package com.makecity.client.domain.auth


data class AuthValidationResponse(
	val isValid: Boolean = false,
	val error: Throwable? = null
)