package com.makecity.core.utils.image

import android.support.annotation.DrawableRes
import android.widget.ImageView
import com.makecity.core.R
import com.makecity.core.data.Presentation


interface ImageRules


@Presentation
data class CommonImageRules(
	val image: ImageView,
	val url: String,
	@DrawableRes val placeholder: Int? = null,
	val withCircle: Boolean = false
): ImageRules
