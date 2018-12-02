package com.makecity.core.extenstion

import android.content.Context
import android.content.pm.PackageManager
import android.support.annotation.StringDef
import android.support.v4.app.ActivityCompat


@DisplayType
val Context.displayType: String
	get() {
		resources.displayMetrics.density.let {
			return when {
				it >= 3.0 -> XXHDPI
				it >= 2.0 -> XHDPI
				it >= 1.5 -> HDPI
				else -> MDPI
			}
		}
	}


@StringDef(value = [MDPI, HDPI, XHDPI, XXHDPI])
annotation class DisplayType

const val MDPI = "mdpi"
const val HDPI = "hdpi"
const val XHDPI = "xhdpi"
const val XXHDPI = "xxhdpi"


inline fun Context.checkAnyPermission(vararg permissions: String, crossinline closure: () -> Unit) {
	val isEverythingDenied = permissions.all {
		ActivityCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
	}

	if (!isEverythingDenied) closure()
}
