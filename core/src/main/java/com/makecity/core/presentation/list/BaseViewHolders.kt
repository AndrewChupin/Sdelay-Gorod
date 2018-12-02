package com.makecity.core.presentation.list

import android.support.annotation.CallSuper
import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.extensions.LayoutContainer


abstract class BaseViewHolder<Item>(
	override val containerView: View
) : RecyclerView.ViewHolder(containerView), LayoutContainer, Bindable<Item> {

	@CallSuper
	override fun bind(item: Item) {
		this.item = item
	}
}


abstract class ClickableViewHolder<Item>(
	containerView: View,
	override val itemClickDelegate: (Item) -> Unit
) : BaseViewHolder<Item>(containerView), ClickableItem<Item> {

	init {
		containerView.setOnClickListener {
			itemClickDelegate(item)
		}
	}

}
