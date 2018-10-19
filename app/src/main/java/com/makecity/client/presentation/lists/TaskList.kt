package com.makecity.client.presentation.lists

import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.makecity.client.BuildConfig
import com.makecity.client.R
import com.makecity.client.data.task.Task
import com.makecity.client.utils.DateHelper
import com.makecity.core.extenstion.checkNotEmpty
import com.makecity.core.extenstion.isVisible
import com.makecity.core.extenstion.joinPath
import com.makecity.core.presentation.list.BaseMultiplyAdapter
import com.makecity.core.presentation.list.BaseViewHolder
import com.makecity.core.presentation.list.ClickableViewHolder
import com.makecity.core.utils.image.CommonImageRules
import com.makecity.core.utils.image.ImageManager
import kotlinx.android.synthetic.main.item_feed_new.*
import java.util.*


class TaskAdapter(
	private val imageManager: ImageManager,
	private val itemDelegate: (Task) -> Unit
) : BaseMultiplyAdapter<Task, BaseViewHolder<Task>>() {

	override var data: List<Task> = emptyList()

	override fun onCreateViewHolder(parent: ViewGroup, position: Int): BaseViewHolder<Task> = TaskViewHolder(
		imageManager = imageManager,
		containerView = LayoutInflater.from(parent.context).inflate(R.layout.item_feed_new, parent, false),
		itemClickDelegate = itemDelegate
	)

	override fun getItemCount(): Int = data.size

	override fun onBindViewHolder(holder: BaseViewHolder<Task>, position: Int) = holder.bind(data[position])

	override fun updateData(data: List<Task>) {
		this.data = data
	}
}

class TaskViewHolder(
	containerView: View,
	override val itemClickDelegate: (Task) -> Unit,
	private val imageManager: ImageManager
) : ClickableViewHolder<Task>(containerView, itemClickDelegate) {

	override lateinit var item: Task

	init {
		feed_item_like.setOnClickListener {
			// TODO
		}
	}

	override fun bind(item: Task) {
		super.bind(item)
		this.item = item

		item.apply {
			feed_item_time.text = DateHelper.convertDateToFormat(Date(updatedTime))
			feed_item_title.text = title
			feed_item_content.text = text
			feed_item_like.text = likeCounts.toString()
			feed_item_comments.text = commentsCount.toString()
			feed_item_status.text = status
			changeLikeSelectable(isLiked)

			imageFirst.checkNotEmpty {
				feed_item_image.isVisible = true
				imageManager.apply(CommonImageRules(feed_item_image, it, null, false))
			}
		}
	}

	private fun changeLikeSelectable(isLiked: Boolean) {
		if (isLiked) {
			val image = ContextCompat.getDrawable(containerView.context, R.drawable.ic_favorite_border_gray_24dp)
			feed_item_like.setCompoundDrawablesWithIntrinsicBounds(image, null, null, null)
		} else {
			val image = ContextCompat.getDrawable(containerView.context, R.drawable.ic_favorite_red_24dp)
			feed_item_like.setCompoundDrawablesWithIntrinsicBounds(image, null, null, null)
		}
	}
}