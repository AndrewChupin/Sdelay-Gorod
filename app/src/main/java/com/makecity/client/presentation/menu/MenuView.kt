package com.makecity.client.presentation.menu

import android.content.Context
import android.graphics.PorterDuff
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.v4.content.ContextCompat
import android.widget.FrameLayout
import com.makecity.client.R
import kotlinx.android.synthetic.main.item_menu.view.*


class MenuView(
	context: Context,
	@StringRes private val textId: Int,
	@DrawableRes private val imageId: Int,
	private val clickListener: () -> Unit = {}
): FrameLayout(context) {

	init {
		inflate(getContext(), R.layout.item_menu, this)

		menu_title.setText(textId)
		menu_image.setImageDrawable(ContextCompat.getDrawable(context, imageId))
		menu_item_container.setOnClickListener { clickListener() }
	}

	private fun setStyle(styleType: MenuItemStyle) = when(styleType) {
		MenuItemStyle.DEFAULT -> {
			menu_image.clearColorFilter()
			menu_title.setTextColor(ContextCompat.getColor(context, R.color.text_dark_main))
		}
		MenuItemStyle.PRIMARY -> {
			menu_image.setColorFilter(R.color.colorAccent, PorterDuff.Mode.SRC_IN)
			menu_title.setTextColor(ContextCompat.getColor(context, R.color.colorAccent))

		}
		MenuItemStyle.DANGEROUS -> {
			menu_image.setColorFilter(R.color.colorDangerous, PorterDuff.Mode.SRC_IN)
			menu_title.setTextColor(ContextCompat.getColor(context, R.color.colorDangerous))
		}
	}
}

enum class MenuItemStyle {
	DEFAULT, PRIMARY, DANGEROUS
}