package com.makecity.client.utils

import com.makecity.core.extenstion.joinPath
import com.makecity.core.utils.Symbols


object ImageHelper {

	 fun concatImage(baseUrl: String, imageName: String): String {
		 return if (imageName.length > 3) {
			 baseUrl.joinPath(
				 Symbols.SLASH,
				 imageName.substring(0, 2),
				 imageName.substring(2, 4),
				 imageName
			 )
		 } else {
			 Symbols.EMPTY
		 }
	 }
 }