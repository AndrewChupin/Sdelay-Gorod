package com.makecity.client.utils

import java.text.SimpleDateFormat
import java.util.*

object DateHelper {

	fun convertDateToFormat(date: Date, locale: Locale = Locale.getDefault()): String {
		return SimpleDateFormat("dd MMMM yyyy в HH:mm", locale).format(date) // TODO LATE
	}
}