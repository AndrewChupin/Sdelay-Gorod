package com.makecity.client.app

import android.util.Log


fun log(message: String, tag: String = "Logos") {
	Log.d(tag, message)
}

fun log(error: Throwable, tag: String = "Logos") {
	Log.e(tag, "Error", error)
}