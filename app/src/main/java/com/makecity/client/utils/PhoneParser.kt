package com.makecity.client.utils

import com.makecity.core.utils.Symbols


class PhoneParser private constructor() {

	init {
		throw IllegalStateException("Can't call constructor")
	}

	companion object {

		private val PHONE_SUFFIX = "+7"

		fun parse(phone: String): String = if (!phone.startsWith(PHONE_SUFFIX)) {
			phone
		} else phone.replace(PHONE_SUFFIX, Symbols.EMPTY)
	}
}
