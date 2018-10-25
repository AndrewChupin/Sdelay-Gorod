package com.makecity.client.presentation.lists

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.makecity.client.R
import com.makecity.client.presentation.camera.Image
import com.makecity.core.presentation.list.BaseMultiplyAdapter
import com.makecity.core.presentation.list.BaseViewHolder
import com.makecity.core.utils.image.CommonImageRules
import com.makecity.core.utils.image.ImageManager
import kotlinx.android.synthetic.main.item_camera_photo.*


interface ImagesListDelegate {
	fun onClickImage(image: Image)
	fun onDeleteImage(image: Image)
}

class ImagesAdapter(
	private val imageManager: ImageManager,
	private val delegate: ImagesListDelegate
): BaseMultiplyAdapter<Image, ImageViewHolder>() {

	override var data: List<Image> = emptyList()

	override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ImageViewHolder = ImageViewHolder(
		containerView = LayoutInflater.from(p0.context).inflate(R.layout.item_camera_photo, p0, false),
		delegate = delegate,
		imageManager = imageManager
	)

	override fun getItemCount(): Int = data.size

	override fun onBindViewHolder(p0: ImageViewHolder, p1: Int) = p0.bind(data[p1])
}


class ImageViewHolder(
	containerView: View,
	private val imageManager: ImageManager,
	delegate: ImagesListDelegate
) : BaseViewHolder<Image>(containerView) {

	companion object {
		private val RANGE_ROTATION_ODD_ITEM = 75..87
		private val RANGE_ROTATION_EDEN_ITEM = 93..105
	}

	override lateinit var item: Image

	init {
		item_camera_image.setOnClickListener {
			delegate.onClickImage(item)
		}

		item_camera_close.setOnClickListener {
			delegate.onDeleteImage(item)
		}
	}

	override fun bind(item: Image) {
		super.bind(item)
		imageManager.apply(CommonImageRules(item_camera_image, item.path))
	}
}