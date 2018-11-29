package com.makecity.core.utils

import android.util.Log


fun log(message: String, tag: String = "Logos") {
	Log.d(tag, message)
}

fun log(error: Throwable, message: String = "Unknown error", tag: String = "Logos") {
	Log.e(tag, message, error)
}