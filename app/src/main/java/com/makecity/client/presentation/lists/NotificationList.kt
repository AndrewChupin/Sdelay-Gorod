package com.makecity.client.presentation.lists

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.makecity.client.R
import com.makecity.core.presentation.list.BaseMultiplyAdapter
import com.makecity.core.presentation.list.BaseViewHolder
import com.makecity.core.presentation.list.ClickableViewHolder
import kotlinx.android.synthetic.main.item_notification.*

data class Notification(
	val title: String,
	val addressee: String,
	val date: String
)

class NotificationAdapter(
	override var data: List<Notification> = emptyList(),
	private val itemDelegate: (Notification) -> Unit
) : BaseMultiplyAdapter<Notification, BaseViewHolder<Notification>>() {

	override fun onCreateViewHolder(parent: ViewGroup, position: Int): BaseViewHolder<Notification> = NotificationViewHolder(
		containerView = LayoutInflater.from(parent.context).inflate(R.layout.item_notification, parent, false),
		itemClickDelegate = itemDelegate
	)

	override fun getItemCount(): Int = data.size

	override fun onBindViewHolder(holder: BaseViewHolder<Notification>, position: Int) = holder.bind(data[position])

	override fun updateData(data: List<Notification>) {
		this.data = data
	}
}

class NotificationViewHolder(
	containerView: View,
	override val itemClickDelegate: (Notification) -> Unit
) : ClickableViewHolder<Notification>(containerView, itemClickDelegate) {

	override lateinit var item: Notification

	override fun bind(item: Notification) {
		super.bind(item)

		this.item = item

		notification_item_addressee.text = item.addressee
		notification_item_content.text = item.title
		notification_item_date.text = item.date
	}
}