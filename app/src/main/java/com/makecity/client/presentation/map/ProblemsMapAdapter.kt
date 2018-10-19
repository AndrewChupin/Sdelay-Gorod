package com.makecity.client.presentation.map

import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.makecity.client.R
import com.makecity.client.data.task.Task
import com.makecity.client.utils.DateHelper
import com.makecity.core.presentation.list.BaseMultiplyAdapter
import com.makecity.core.presentation.list.BaseViewHolder
import com.makecity.core.presentation.list.ClickableViewHolder
import kotlinx.android.synthetic.main.item_problem_bottom.*
import java.util.*

class ProblemsMapAdapter(
	private val itemDelegate: (Task) -> Unit
) : BaseMultiplyAdapter<Task, BaseViewHolder<Task>>() {

	override var data: List<Task> = emptyList()

	override fun onCreateViewHolder(parent: ViewGroup, position: Int): BaseViewHolder<Task> = ProblemMapViewHolder(
		containerView = LayoutInflater.from(parent.context).inflate(R.layout.item_problem_bottom, parent, false),
		itemClickDelegate = itemDelegate
	)

	override fun getItemCount(): Int = data.size

	override fun onBindViewHolder(holder: BaseViewHolder<Task>, position: Int) = holder.bind(data[position])

	override fun updateData(data: List<Task>) {
		this.data = data
	}
}

class ProblemMapViewHolder(
	containerView: View,
	override val itemClickDelegate: (Task) -> Unit
) : ClickableViewHolder<Task>(containerView, itemClickDelegate) {

	override lateinit var item: Task

	init {
		problem_bottom_item_like.setOnClickListener {
			// TODO
		}
	}

	override fun bind(item: Task) {
		super.bind(item)
		this.item = item

		item.apply {
			problem_bottom_item_time.text = DateHelper.convertDateToFormat(Date(updatedTime))
			problem_bottom_item_title.text = title
			problem_bottom_item_content.text = text
			problem_bottom_item_like.text = likeCounts.toString()
			problem_bottom_item_comments.text = commentsCount.toString()
			problem_bottom_item_status.text = status
			changeLikeSelectable(isLiked)
		}
	}

	private fun changeLikeSelectable(isLiked: Boolean) {
		if (isLiked) {
			val image = ContextCompat.getDrawable(containerView.context, R.drawable.ic_favorite_border_gray_24dp)
			problem_bottom_item_like.setCompoundDrawablesWithIntrinsicBounds(image, null, null, null)
		} else {
			val image = ContextCompat.getDrawable(containerView.context, R.drawable.ic_favorite_red_24dp)
			problem_bottom_item_like.setCompoundDrawablesWithIntrinsicBounds(image, null, null, null)
		}
	}
}