package com.makecity.core.extenstion

import android.view.View


@Suppress("RecursivePropertyAccessor")
var View.isVisible: Boolean
	get() = visibility == View.VISIBLE
	set(value) {
		visibility = if (value) View.VISIBLE else View.GONE
	}

@Suppress("RecursivePropertyAccessor")
fun View.changeVisibleWithAnimation(isVisible: Boolean) {
	if (isVisible) {
		this.showWithScale()
	} else {
		this.hideWithScale(scaleX = 1.0f, scaleY = 1.0f)
	}
}


@Suppress("RecursivePropertyAccessor")
fun View.hideWithScale(duration: Long = 250, scaleX: Float = 0.5f, scaleY: Float = 0.5f) {
	if (alpha > 0f) {
		animate()
			.scaleX(scaleX)
			.scaleY(scaleY)
			.alpha(0f)
			.setDuration(duration)
			.start()
	}
}


@Suppress("RecursivePropertyAccessor")
fun View.showWithScale(duration: Long = 250) {
	if (alpha == 0f) {
		animate()
			.scaleX(1f)
			.scaleY(1f)
			.alpha(1f)
			.setDuration(duration)
			.start()
	}
}
