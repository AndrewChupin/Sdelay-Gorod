package com.makecity.core.presentation.list


interface ClickableItem<in Item> {
	val itemClickDelegate: (Item) -> Unit
}


interface Bindable<Item> {
	var item: Item
	fun bind(item: Item)
}
