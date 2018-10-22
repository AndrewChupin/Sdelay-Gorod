package com.makecity.client.data.auth

import com.makecity.core.domain.Mapper


class AuthNextStep: Mapper<String, NextAuthStep> {

	companion object {
		private const val SMS = "sms"
	}

	override fun transform(entity: String): NextAuthStep = when (entity) {
		SMS -> NextAuthStep(AuthType.SMS)
		else -> throw IllegalArgumentException("Not founded stap with name $entity")
	}
}