package com.makecity.client.presentation.aimation

import android.animation.Animator
import android.view.animation.Animation


interface EndAnimatorListener: Animator.AnimatorListener {
	override fun onAnimationRepeat(animation: Animator?) {}
	override fun onAnimationCancel(animation: Animator?) {}
	override fun onAnimationStart(animation: Animator?) {}
}

interface EndAnimationListener: Animation.AnimationListener {
	override fun onAnimationStart(animation: Animation?) {}
	override fun onAnimationRepeat(animation: Animation?) {}
}