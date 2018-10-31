package com.makecity.client.utils.saver

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.util.Log
import com.makecity.client.app.AppConst.MAX_IMAGE_SIZE
import com.makecity.core.utils.saver.FileSaver
import com.makecity.core.utils.saver.SaverRequest
import io.fotoapparat.exception.FileSaveException
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject


data class BitmapSaverRequest(
	override val file: File
) : SaverRequest


class FileSaverBitmap @Inject constructor() : FileSaver {

	override fun save(request: SaverRequest) {
		val file = request.file
		val bitmap = BitmapFactory.decodeFile(file.absolutePath)

		if (file.length() > MAX_IMAGE_SIZE) {
			val factor = (MAX_IMAGE_SIZE.toFloat() / file.length().toFloat()) * 100
			FileOutputStream(file)
				.buffered()
				.use { os ->
					bitmap.compress(Bitmap.CompressFormat.JPEG, factor.toInt(), os)
					writeExifOrientation(file, 0)
				}

		}
		Log.d("Logos", "New file size ${file.length()/1024}")
	}

	private fun writeExifOrientation(file: File, rotationDegrees: Int) {
		try {
			ExifInterface(file.path).apply {
				setAttribute(
					ExifInterface.TAG_ORIENTATION,
					toExifOrientation(rotationDegrees).toString()
				)
				saveAttributes()
			}
		} catch (e: IOException) {
			throw FileSaveException(e)
		}
	}

	private fun toExifOrientation(rotationDegrees: Int): Int {
		val compensationRotationDegrees = (360 - rotationDegrees) % 360

		return when (compensationRotationDegrees) {
			90 -> ExifInterface.ORIENTATION_ROTATE_90
			180 -> ExifInterface.ORIENTATION_ROTATE_180
			270 -> ExifInterface.ORIENTATION_ROTATE_270
			else -> ExifInterface.ORIENTATION_NORMAL
		}
	}
}