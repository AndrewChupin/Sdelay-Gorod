package com.makecity.client.presentation.aimation.incorrect

import android.animation.Animator
import android.animation.AnimatorSet


data class IncorrectAnimation(
	val animatorSet: AnimatorSet,
	val listener: Animator.AnimatorListener?
)