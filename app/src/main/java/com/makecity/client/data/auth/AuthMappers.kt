package com.makecity.client.data.auth

import com.makecity.core.domain.Mapper
import javax.inject.Inject


class AuthNextStepMapper @Inject constructor(): Mapper<String, NextAuthStep> {

	companion object {
		private const val SMS = "/auth/get-reg-token"
		private const val CREATE_PASSWORD = "/auth/set-pass"
	}

	override fun transform(entity: String): NextAuthStep = when (entity) {
		SMS -> NextAuthStep(AuthType.SMS)
		CREATE_PASSWORD -> NextAuthStep(AuthType.CREATE_PASSWORD)
		else -> throw IllegalArgumentException("Not founded stap with name $entity")
	}
}