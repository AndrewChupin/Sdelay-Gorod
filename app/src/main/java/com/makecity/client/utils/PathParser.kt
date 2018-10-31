package com.makecity.client.utils

import android.content.ContentResolver
import android.net.Uri
import android.provider.MediaStore

object PathParser {

	fun parseMedia(contentResolver: ContentResolver, uri: Uri): String {
		val projection = arrayOf(MediaStore.Images.Media.DATA)
		return contentResolver
			.query(uri, projection, null, null, null)
			.use {
				it.moveToFirst()
				val columnIndex = it.getColumnIndex(projection[0])
				it.getString(columnIndex)
			}
	}
}