package com.makecity.core.utils.image

import android.annotation.SuppressLint
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.makecity.core.R
import com.makecity.core.utils.image.CommonImageRules
import com.makecity.core.utils.image.ImageRules
import javax.inject.Inject


/**
 * Wrapper on image loader for [ImageView]
 * @author Andrew Chupin
 */
interface ImageManager {
	/**
	 * @param url - it may be HTTP or File url to resource witch will be load to [imageView] with something library
	 */
	fun apply(rules: ImageRules)
}


class CommonImageManager @Inject constructor(): ImageManager {

	override fun apply(rules: ImageRules) = when (rules) {
		is CommonImageRules -> loadGlideImage(rules)
		else -> throw IllegalStateException("rules with class ${rules.javaClass.canonicalName} unsupported")
	}

	@SuppressLint("CheckResult")
	private fun loadGlideImage(rules: CommonImageRules) {
		var request = Glide
			.with(rules.image)
			.load(rules.url)
		val options = RequestOptions()

		if (rules.withCircle) {
			options.circleCrop()
		}

		rules.placeholder?.let {
			options.placeholder(it)
		}

		request = request.apply(options)
		request.into(rules.image)
	}
}
