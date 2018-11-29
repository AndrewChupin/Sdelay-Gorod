package com.makecity.core.plugin.channel

interface Message

interface PrimaryMessage: Message


sealed class DefaultMessage: PrimaryMessage {
	data class ShowError(
		val error: Throwable? = null,
		val message: String? = null
	) : DefaultMessage()

	data class ShowMessage(
		val message: String
	) : DefaultMessage()

	object ClearData : DefaultMessage()
}