package com.makecity.client.presentation.aimation

import android.view.animation.Interpolator


class SpringInterpolator(private val tension: Float) : Interpolator {

	override fun getInterpolation(v: Float): Float {
		return Math.pow(2.0, (-4 * v).toDouble()).toFloat() * Math.sin((v - tension / 4) * (2 * Math.PI) / tension).toFloat() + 1
	}
}
