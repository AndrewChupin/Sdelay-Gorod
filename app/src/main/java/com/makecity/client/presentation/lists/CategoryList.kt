package com.makecity.client.presentation.lists

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.makecity.client.R
import com.makecity.client.data.category.Category
import com.makecity.core.presentation.list.BaseMultiplyAdapter
import com.makecity.core.presentation.list.ClickableViewHolder
import kotlinx.android.synthetic.main.item_category.*


class CategoryAdapter(
	override var data: List<Category> = emptyList()
) : BaseMultiplyAdapter<Category, CategoryViewHolder>() {

	override fun onCreateViewHolder(view: ViewGroup, type: Int): CategoryViewHolder = CategoryViewHolder(
		LayoutInflater.from(view.context).inflate(R.layout.item_category, view, false)
	) {} // TODO

	override fun getItemCount(): Int = data.size

	override fun onBindViewHolder(viewHolder: CategoryViewHolder, position: Int)
		= viewHolder.bind(data[position])

	override fun updateData(data: List<Category>) {
		this.data = data
	}
}


class CategoryViewHolder(
	override val containerView: View,
	override val itemClickDelegate: (Category) -> Unit
) : ClickableViewHolder<Category>(containerView, itemClickDelegate) {

	override lateinit var item: Category

	override fun bind(item: Category) {
		super.bind(item)
		item_category_name.text = item.name
	}
}