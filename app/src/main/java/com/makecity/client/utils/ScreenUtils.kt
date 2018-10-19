package com.makecity.client.utils

import android.content.res.Resources


object ScreenUtils {

    fun convertPixelsToDp(px: Float): Float {
        val metrics = Resources.getSystem().displayMetrics
        val dp = px / (metrics.densityDpi / 160f)
        return Math.round(dp).toFloat()
    }


    fun convertDpToPixel(dp: Float): Float {
        val metrics = Resources.getSystem().displayMetrics
        val px = dp * (metrics.densityDpi / 160f)
        return Math.round(px).toFloat()
    }

    val screenWidth: Int
        get() {
            val metrics = Resources.getSystem().displayMetrics
            return metrics.widthPixels
        }

    val screenHeight: Int
        get() {
            val metrics = Resources.getSystem().displayMetrics
            return metrics.heightPixels
        }
}