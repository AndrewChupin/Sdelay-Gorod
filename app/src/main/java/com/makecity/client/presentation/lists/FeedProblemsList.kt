package com.makecity.client.presentation.lists

import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.makecity.client.R
import com.makecity.client.data.task.ProblemStatus
import com.makecity.client.data.task.Task
import com.makecity.client.utils.DateHelper
import com.makecity.core.extenstion.checkNotEmpty
import com.makecity.core.extenstion.textWithExecutable
import com.makecity.core.presentation.list.BaseMultiplyAdapter
import com.makecity.core.presentation.list.BaseViewHolder
import com.makecity.core.presentation.list.ClickableViewHolder
import com.makecity.core.utils.Symbols.EMPTY
import com.makecity.core.utils.diff.BaseDiffUtil
import com.makecity.core.utils.diff.BaseIdenticalDiffUtil
import com.makecity.core.utils.diff.SingleDiffUtil
import com.makecity.core.utils.image.CommonImageRules
import com.makecity.core.utils.image.ImageManager
import kotlinx.android.synthetic.main.item_feed_new.*
import kotlinx.android.synthetic.main.item_problem_history.*
import java.util.*

interface TaskDelegate {
	fun likeClicked(task: Task)
}

class TaskAdapter(
	private val imageManager: ImageManager,
	private val delegate: TaskDelegate,
	private val itemDelegate: (Task) -> Unit
) : BaseMultiplyAdapter<Task, BaseViewHolder<Task>>(
	diffUtilCallback = BaseIdenticalDiffUtil()
) {

	override var data: List<Task> = emptyList()

	override fun onCreateViewHolder(parent: ViewGroup, position: Int): BaseViewHolder<Task> = TaskViewHolder(
		imageManager = imageManager,
		containerView = LayoutInflater.from(parent.context).inflate(R.layout.item_feed_new, parent, false),
		likeClickedDelegate = delegate::likeClicked,
		itemClickDelegate = itemDelegate
	)

	override fun getItemCount(): Int = data.size

	override fun onBindViewHolder(holder: BaseViewHolder<Task>, position: Int) = holder.bind(data[position])
}

class TaskViewHolder(
	containerView: View,
	override val itemClickDelegate: (Task) -> Unit,
	private val likeClickedDelegate: (Task) -> Unit,
	private val imageManager: ImageManager
) : ClickableViewHolder<Task>(containerView, itemClickDelegate) {

	override lateinit var item: Task

	init {
		feed_item_like.setOnClickListener {
			likeClickedDelegate(item)
		}
	}

	override fun bind(item: Task) {
		super.bind(item)
		this.item = item

		item.apply {
			feed_item_time.text = DateHelper.convertDateToFormat(Date(updatedTime))
			feed_item_title.text = text
			feed_item_content.textWithExecutable(listOf(
				categories.main.name.capitalize(),
				categories.sub?.name?.capitalize() ?: EMPTY
			)) {

			}

			feed_item_like.text = likeCounts.toString()
			feed_item_comments.text = commentsCount.toString()
			feed_item_status.text = status
			val shapeDrawable = feed_item_status.background as GradientDrawable
			shapeDrawable.setColor(when (statusType) {
				ProblemStatus.NEW -> ContextCompat.getColor(containerView.context, R.color.colorNew)
				ProblemStatus.IN_PROGRESS -> ContextCompat.getColor(containerView.context, R.color.colorAccent)
				ProblemStatus.DONE -> ContextCompat.getColor(containerView.context, R.color.colorSuccess)
				ProblemStatus.CANCELED -> ContextCompat.getColor(containerView.context, R.color.colorDangerous)
				ProblemStatus.REJECT -> ContextCompat.getColor(containerView.context, R.color.colorDangerous)
			})
			feed_item_author_name.text = author.userName
			changeLikeSelectable(isLiked)

			author.image.checkNotEmpty {
				imageManager.apply(CommonImageRules(feed_item_author_photo, it, R.drawable.placeholder_face, true))
			}
		}
	}

	private fun changeLikeSelectable(isLiked: Boolean) {
		val image = if (isLiked) {
			ContextCompat.getDrawable(containerView.context, R.drawable.ic_favorite_red_24dp)
		} else {
			ContextCompat.getDrawable(containerView.context, R.drawable.ic_favorite_border_gray_24dp)
		}

		feed_item_like.setCompoundDrawablesWithIntrinsicBounds(image, null, null, null)
	}
}