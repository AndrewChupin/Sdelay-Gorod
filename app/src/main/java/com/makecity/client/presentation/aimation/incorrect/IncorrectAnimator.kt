package com.makecity.client.presentation.aimation.incorrect

import android.animation.Animator
import android.view.View
import com.makecity.client.presentation.aimation.SimpleAnimatorListener
import com.makecity.client.presentation.aimation.SpringInterpolator
import com.makecity.client.utils.AnimationUtils
import com.makecity.client.utils.VibrationUtils


object IncorrectAnimator : SimpleAnimatorListener {

	const val VIBRATION_DURATION = 100L // Ms
	const val TENSION_DURATION_DEFAULT = 0.40f
	const val ANIMATION_DURATION_DEFAULT = 1200

	private val interpolator = SpringInterpolator(TENSION_DURATION_DEFAULT)
	private val animationList: MutableList<IncorrectAnimation> = mutableListOf()

	fun add(
		view: View,
		duration: Long = VIBRATION_DURATION,
		animatorListener: Animator.AnimatorListener? = null
	) {
		VibrationUtils.vibrate(view.context, duration)

		val set = AnimationUtils.createSpringAnimation(
			interpolator = interpolator,
			view = view,
			duration = ANIMATION_DURATION_DEFAULT
		)

		animatorListener?.let {
			set.addListener(it)
		}

		animationList.add(IncorrectAnimation(
			animatorSet = set,
			listener = animatorListener
		))

		set.start()
	}

	fun destroy() {
		animationList.forEach {
			it.animatorSet.removeAllListeners()
		}
	}
}