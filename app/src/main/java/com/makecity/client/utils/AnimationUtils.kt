package com.makecity.client.utils

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.Interpolator
import com.makecity.core.utils.ScreenUtils

object AnimationUtils {

	private const val ANIMATION_OFFSET_DEFAULT = 24f

	fun createSpringAnimation(interpolator: Interpolator,
							  view: View,
							  duration: Int): AnimatorSet {
		val set = AnimatorSet()

		val springOffset = ScreenUtils.convertDpToPixel(ANIMATION_OFFSET_DEFAULT)

		val viewSpringAnimator = AnimationUtils.getTranslationXAnimator(
			view, springOffset, 0f, 0, duration.toLong())

		viewSpringAnimator.interpolator = interpolator

		set.play(viewSpringAnimator)
		return set
	}

	private fun getTranslationXAnimator(view: View,
										startTranslationX: Float,
										targetTranslationX: Float,
										startDelay: Long, duration: Long): ObjectAnimator {
		val animator = ObjectAnimator.ofFloat(view, View.TRANSLATION_X,
			startTranslationX, targetTranslationX)
			.setDuration(duration)
		animator.startDelay = startDelay
		return animator
	}
}
