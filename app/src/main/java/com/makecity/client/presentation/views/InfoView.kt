package com.makecity.client.presentation.views

import android.content.Context
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.widget.FrameLayout
import com.makecity.client.R
import kotlinx.android.synthetic.main.item_info.view.*

class InfoView(
	context: Context,
	@DrawableRes private val imageId: Int,
	title: String,
	value: String
): FrameLayout(context) {

	init {
		inflate(getContext(), R.layout.item_info, this)
		item_info_icon.setImageDrawable(ContextCompat.getDrawable(context, imageId))
		item_info_title.text = title
		item_info_value.text = value
	}
}