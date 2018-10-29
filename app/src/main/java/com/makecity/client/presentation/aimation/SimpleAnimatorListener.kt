package com.makecity.client.presentation.aimation

import android.animation.Animator


interface SimpleAnimatorListener : Animator.AnimatorListener {

	override fun onAnimationStart(animation: Animator) { /* default */
	}

	override fun onAnimationEnd(animation: Animator) { /* default */
	}

	override fun onAnimationCancel(animation: Animator) { /* default */
	}

	override fun onAnimationRepeat(animation: Animator) { /* default */
	}
}
