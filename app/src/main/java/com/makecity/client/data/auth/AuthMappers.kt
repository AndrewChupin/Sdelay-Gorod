package com.makecity.client.data.auth

import com.makecity.core.domain.Mapper
import javax.inject.Inject


class AuthNextStepMapper @Inject constructor(): Mapper<String, NextAuthStep> {

	companion object {
		private const val SMS = "get-reg-token"
		private const val CREATE_PASSWORD = "set-pass"
		private const val LOGIN = "login"
	}

	override fun transform(entity: String): NextAuthStep = when (entity) {
		SMS -> NextAuthStep(AuthType.SMS)
		CREATE_PASSWORD -> NextAuthStep(AuthType.CREATE_PASSWORD)
		LOGIN -> NextAuthStep(AuthType.PASSWORD)
		else -> throw IllegalArgumentException("Not founded stap with name $entity")
	}
}