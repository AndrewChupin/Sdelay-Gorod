package com.makecity.client.utils

import com.makecity.client.BuildConfig
import com.makecity.core.data.entity.Location
import com.makecity.core.utils.ScreenUtils

object GoogleApiHelper {
	//
	fun createStaticUrl(location: Location): String {
		// TODO
		ScreenUtils.screenWidth
		var width = ScreenUtils.screenWidth - ScreenUtils.convertDpToPixel(32f)
		var height = ScreenUtils.convertDpToPixel(150f)

		if (width > 640) {
			height = height/width * 640
			width = 640f
		}

		return "${BuildConfig.URL_GOOGLE_API}staticmap?" +
			"center=${location.latitude},${location.longitude}" +
			"&zoom=16" +
			"&size=${width.toInt()}x${height.toInt()}" +
			"&maptype=roadmap" +
			"&language=ru" +
			"&markers=color:blue%7C${location.latitude},${location.longitude}" +
			"&key=${BuildConfig.GOOGLE_MAPS_KEY}"
	}

}