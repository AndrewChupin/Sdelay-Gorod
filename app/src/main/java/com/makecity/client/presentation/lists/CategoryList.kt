package com.makecity.client.presentation.lists

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.makecity.client.R
import com.makecity.core.presentation.list.BaseMultiplyAdapter
import com.makecity.core.presentation.list.ClickableViewHolder
import kotlinx.android.synthetic.main.item_category.*


class CategoryAdapter(
	private val itemClickDelegate: (Pair<Long, String>) -> Unit
) : BaseMultiplyAdapter<Pair<Long, String>, CategoryViewHolder>() {

	override var data: List<Pair<Long, String>> = emptyList()

	override fun onCreateViewHolder(view: ViewGroup, type: Int): CategoryViewHolder = CategoryViewHolder(
		LayoutInflater.from(view.context).inflate(R.layout.item_category, view, false),
		itemClickDelegate
	)

	override fun getItemCount(): Int = data.size

	override fun onBindViewHolder(viewHolder: CategoryViewHolder, position: Int) = viewHolder.bind(data[position])
}


class CategoryViewHolder(
	override val containerView: View,
	override val itemClickDelegate: (Pair<Long, String>) -> Unit
) : ClickableViewHolder<Pair<Long, String>>(containerView, itemClickDelegate) {

	override lateinit var item: Pair<Long, String>

	override fun bind(item: Pair<Long, String>) {
		super.bind(item)
		item_category_name.text = item.second
	}
}