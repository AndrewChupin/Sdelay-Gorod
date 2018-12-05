package com.makecity.client.presentation.lists

import android.app.job.JobService
import android.graphics.drawable.GradientDrawable
import android.support.v4.app.JobIntentService
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.makecity.client.R
import com.makecity.client.data.comments.Comment
import com.makecity.client.data.problem.ProblemDetail
import com.makecity.client.data.task.History
import com.makecity.client.data.task.ProblemStatus
import com.makecity.client.data.task.Task
import com.makecity.client.presentation.lists.TaskDetailAdapter.Companion.ADDITIONAL_CELLS_COUNT
import com.makecity.client.presentation.views.InfoView
import com.makecity.client.utils.DateHelper
import com.makecity.client.utils.GoogleApiHelper
import com.makecity.core.data.entity.Location
import com.makecity.core.extenstion.checkNotEmpty
import com.makecity.core.extenstion.isVisible
import com.makecity.core.presentation.list.BaseSingleAdapters
import com.makecity.core.presentation.list.BaseViewHolder
import com.makecity.core.presentation.list.ClickableViewHolder
import com.makecity.core.utils.diff.SingleDiffUtil
import com.makecity.core.utils.image.CommonImageRules
import com.makecity.core.utils.image.ImageManager
import com.makecity.core.utils.log
import kotlinx.android.synthetic.main.fragment_auth.*
import kotlinx.android.synthetic.main.item_comment.*
import kotlinx.android.synthetic.main.item_history.*
import kotlinx.android.synthetic.main.item_problem_content.*
import kotlinx.android.synthetic.main.item_problem_info.*
import kotlinx.android.synthetic.main.item_problem_location.*
import kotlinx.android.synthetic.main.item_problem_photo.*
import ru.tinkoff.decoro.MaskImpl
import ru.tinkoff.decoro.slots.PredefinedSlots
import ru.tinkoff.decoro.watchers.MaskFormatWatcher
import java.util.*


interface TaskDetailsDelegate {
	fun showMoreCommentsClicked()
	fun likeClicked(task: Task)
}


class TaskDetailAdapter(
	private val imageManager: ImageManager,
	private val problemDelegate: (Task) -> Unit,
	private val commentDelegate: (Comment) -> Unit,
	private val delegate: TaskDetailsDelegate
) : BaseSingleAdapters<ProblemDetail>(ProblemDiffUtils()) {

	companion object {
		/**
		 * 0 - content
		 * 1 - location - optional
		 * 2 - info
		 * 3 - photos - optional
		 * 4 - comments title
		 * 5 - show more - optional
		 */
		const val ADDITIONAL_CELLS_COUNT = 6
	}

	override var data: ProblemDetail? = null

	override fun onCreateViewHolder(
		parent: ViewGroup, type: Int
	): BaseViewHolder<*> = when (type) {
		R.layout.item_problem_content -> ProblemViewHolder(
			containerView = LayoutInflater.from(parent.context).inflate(R.layout.item_problem_content, parent, false),
			itemClickDelegate = problemDelegate,
			likeClickedDelegate = delegate::likeClicked,
			imageManager = imageManager
		)
		R.layout.item_problem_location -> LocationViewHolder(
			containerView = LayoutInflater.from(parent.context).inflate(R.layout.item_problem_location, parent, false),
			imageManager = imageManager
		)
		R.layout.item_problem_info -> ProblemInfoViewHolder(
			containerView = LayoutInflater.from(parent.context).inflate(R.layout.item_problem_info, parent, false)
		)
		R.layout.item_problem_photo -> ProblemPhotosViewHolder(
			imageManager = imageManager,
			containerView = LayoutInflater.from(parent.context).inflate(R.layout.item_problem_photo, parent, false)
		)
		R.layout.item_problem_title -> ProblemTitleViewHolder(
			containerView = LayoutInflater.from(parent.context).inflate(R.layout.item_problem_title, parent, false)
		)
		R.layout.item_problem_show_more -> ProblemShowMoreViewHolder(
			containerView = LayoutInflater.from(parent.context).inflate(R.layout.item_problem_show_more, parent, false),
			showMoreDelegate = delegate::showMoreCommentsClicked
		)
		R.layout.item_history -> ProblemHistoryViewHolder(
			containerView = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
		)
		else -> CommentViewHolder(
			imageManager = imageManager,
			containerView = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false),
			itemClickDelegate = commentDelegate
		)
	}

	override fun getItemCount(): Int {
		val size = data?.let { it ->
			it.comments.size + ADDITIONAL_CELLS_COUNT
		}
		return size ?: 0
	}

	override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
		when (holder) {
			is CommentViewHolder -> data?.let {
				val offset = ADDITIONAL_CELLS_COUNT - 1 // Show more
				holder.bind(it.comments[position - offset])
			}
			is ProblemViewHolder -> data?.let {
				holder.bind(it.task)
			}
			is ProblemPhotosViewHolder -> data?.let {
				holder.bind(it.task)
			}
			is ProblemInfoViewHolder -> data?.let {
				holder.bind(it.task)
			}
			is LocationViewHolder -> data?.let {
				holder.bind(it.task)
			}
			is ProblemShowMoreViewHolder -> data?.let {
				holder.bind(it.task)
			}
			is ProblemTitleViewHolder -> data?.let {
				holder.bind(it.task)
			}
			is ProblemHistoryViewHolder -> data?.let {
				val commentsSize = data?.comments?.size ?: 0
				val offset = ADDITIONAL_CELLS_COUNT - 1 + commentsSize// Show more
				holder.bind(it.task.history[position - offset])
			}
		}
	}

	override fun getItemViewType(position: Int): Int = when (position) {
		0 -> R.layout.item_problem_content
		1 -> R.layout.item_problem_location
		2 -> R.layout.item_problem_info
		3 -> R.layout.item_problem_photo
		4 -> R.layout.item_problem_title
		itemCount - 1 -> R.layout.item_problem_show_more
		else -> R.layout.item_comment
	}
}


class ProblemViewHolder(
	containerView: View,
	override val itemClickDelegate: (Task) -> Unit,
	private val likeClickedDelegate: (Task) -> Unit,
	private val imageManager: ImageManager
) : ClickableViewHolder<Task>(containerView, itemClickDelegate) {

	override lateinit var item: Task

	init {
		problem_item_like.setOnClickListener {
			likeClickedDelegate(item)
		}
	}

	override fun bind(item: Task) {
		super.bind(item)

		item.apply {
			task_item_title.text = text
			task_item_time.text = DateHelper.convertDateToFormat(Date(updatedTime))
			task_item_status.text = status
			val shapeDrawable = task_item_status.background as GradientDrawable
			shapeDrawable.setColor(when (statusType) {
				ProblemStatus.NEW -> ContextCompat.getColor(containerView.context, R.color.colorNew)
				ProblemStatus.IN_PROGRESS -> ContextCompat.getColor(containerView.context, R.color.colorAccent)
				ProblemStatus.DONE -> ContextCompat.getColor(containerView.context, R.color.colorSuccess)
				ProblemStatus.CANCELED -> ContextCompat.getColor(containerView.context, R.color.colorDangerous)
				ProblemStatus.REJECT -> ContextCompat.getColor(containerView.context, R.color.colorDangerous)
			})
			problem_item_like.text = likeCounts.toString()
			problem_item_author_name.text = author.userName
			changeLikeSelectable(isLiked)

			author.image.checkNotEmpty {
				imageManager.apply(CommonImageRules(problem_item_author_photo, it, R.drawable.placeholder_face, true))
			}
		}
	}

	private fun changeLikeSelectable(isLiked: Boolean) {
		val image = if (isLiked) {
			ContextCompat.getDrawable(containerView.context, R.drawable.ic_favorite_red_24dp)
		} else {
			ContextCompat.getDrawable(containerView.context, R.drawable.ic_favorite_border_gray_24dp)
		}

		problem_item_like.setCompoundDrawablesWithIntrinsicBounds(image, null, null, null)
	}
}

class LocationViewHolder(
	containerView: View,
	private val imageManager: ImageManager
) : BaseViewHolder<Task>(containerView) {
	override lateinit var item: Task

	override fun bind(item: Task) {
		super.bind(item)

		if (item.latitude == 0.0 && item.longitude == 0.0) {
			containerView.isVisible = false
			containerView.layoutParams = containerView.layoutParams.apply {
				height = 0
			}
			return
		} else {
			containerView.isVisible = true
			containerView.layoutParams = containerView.layoutParams.apply {
				height = ViewGroup.LayoutParams.WRAP_CONTENT
			}
		}

		item_problem_location_address.text = item.address
		val location = Location(item.latitude, item.longitude)
		val rules = CommonImageRules(item_problem_location_image, GoogleApiHelper.createStaticUrl(location))
		imageManager.apply(rules)
	}
}

class ProblemInfoViewHolder(
	containerView: View
) : BaseViewHolder<Task>(containerView) {

	override lateinit var item: Task

	override fun bind(item: Task) {
		super.bind(item)

		info_container.run {
			removeAllViews()
			item.categories.main.name.checkNotEmpty {
				addView(InfoView(context, R.drawable.ic_archive_gray_24dp, context.getString(R.string.category), it))
			}

			item.categories.sub?.name?.checkNotEmpty {
				addView(InfoView(context, R.drawable.ic_info_gray_24dp, context.getString(R.string.service), it))
			}

			item.companyName.checkNotEmpty {
				addView(InfoView(context, R.drawable.ic_group_gray_24dp, context.getString(R.string.company), it))
			}
			requestLayout()
		}
	}
}

class ProblemPhotosViewHolder(
	containerView: View,
	private val imageManager: ImageManager
) : BaseViewHolder<Task>(containerView) {

	override lateinit var item: Task

	override fun bind(item: Task) {
		super.bind(item)

		if (item.imageFirst.isEmpty()) {
			containerView.isVisible = false
			containerView.layoutParams = containerView.layoutParams.apply {
				height = 0
			}
			return
		} else {
			containerView.isVisible = true
			containerView.layoutParams = containerView.layoutParams.apply {
				height = ViewGroup.LayoutParams.WRAP_CONTENT
			}
		}

		item.apply {
			imageFirst.checkNotEmpty {
				task_image_first.isVisible = true
				imageManager.apply(CommonImageRules(task_image_first, imageFirst))
			}

			imageSecond.checkNotEmpty {
				task_image_second.isVisible = true
				imageManager.apply(CommonImageRules(task_image_second, imageSecond))
			}
		}
	}
}

class ProblemTitleViewHolder(
	containerView: View
) : BaseViewHolder<Task>(containerView) {
	override lateinit var item: Task

	override fun bind(item: Task) {
		super.bind(item)
		if (item.commentsCount == 0) {
			containerView.isVisible = false
			containerView.layoutParams = containerView.layoutParams.apply {
				height = 0
			}
		} else {
			containerView.isVisible = true
			containerView.layoutParams = containerView.layoutParams.apply {
				height = ViewGroup.LayoutParams.WRAP_CONTENT
			}
		}
	}
}

class CommentViewHolder(
	containerView: View,
	override val itemClickDelegate: (Comment) -> Unit,
	private val imageManager: ImageManager
) : ClickableViewHolder<Comment>(containerView, itemClickDelegate) {

	override lateinit var item: Comment

	override fun bind(item: Comment) {
		super.bind(item)

		item.apply {
			comment_date.text = DateHelper.convertDateToFormat(Date(updatedTime))
			comment_user_name.text = when {
				author.userName.isNotEmpty() -> author.userName
				author.phone.isNotEmpty() -> author.phone
				else -> containerView.context.getString(R.string.name_undefined)
			}
			comment_message.text = text

			imageManager.apply(
				CommonImageRules(
					image = comment_image,
					url = author.image,
					placeholder = R.drawable.placeholder_face,
					withCircle = true
				)
			)
		}
	}
}

class ProblemShowMoreViewHolder(
	containerView: View,
	val showMoreDelegate: () -> Unit
) : BaseViewHolder<Task>(containerView) {
	override lateinit var item: Task

	init {
		containerView.setOnClickListener {
			showMoreDelegate()
		}
	}

	override fun bind(item: Task) {
		super.bind(item)

		if (item.commentsCount < 10) {
			containerView.isVisible = false
			containerView.layoutParams = containerView.layoutParams.apply {
				height = 0
			}
		} else {
			containerView.isVisible = true
			containerView.layoutParams = containerView.layoutParams.apply {
				height = ViewGroup.LayoutParams.WRAP_CONTENT
			}
		}
	}
}

class ProblemHistoryViewHolder(
	containerView: View
): BaseViewHolder<History>(containerView) {

	override lateinit var item: History

	override fun bind(item: History) {
		super.bind(item)
		item_history_title.text = item.text
	}
}

/**
 * 0 - content
 * 1 - location - optional
 * 2 - info
 * 3 - photos - optional
 * 4 - comments title
 * 5 - show more - optional
 */
class ProblemDiffUtils : SingleDiffUtil<ProblemDetail>() {

	override fun areItemsTheSame(oldIndex: Int, newIndex: Int): Boolean  = check(oldIndex, newIndex)

	override fun getOldListSize(): Int {
		val old = itemOld ?: return 0
		return old.comments.size + ADDITIONAL_CELLS_COUNT
	}

	override fun getNewListSize(): Int {
		val new = itemNew ?: return 0
		return new.comments.size + ADDITIONAL_CELLS_COUNT
	}

	override fun areContentsTheSame(oldIndex: Int, newIndex: Int): Boolean = check(oldIndex, newIndex)

	private fun check(oldIndex: Int, newIndex: Int): Boolean {
		val old = itemOld ?: return false
		val new = itemNew ?: return false

		if (oldIndex == 0 && newIndex == 0) {
			return old.task.text == new.task.text
		}

		if (oldIndex == 1 && newIndex == 1) {
			return old.task.latitude == new.task.latitude
				&& old.task.longitude == new.task.longitude
		}

		if (oldIndex == 2 && newIndex == 2) {
			return old.task.categories == new.task.categories
				&& old.task.companyName == new.task.companyName
		}

		if (oldIndex == 3 && newIndex == 3) {
			return old.task.imageFirst == new.task.imageFirst && old.task.imageSecond == new.task.imageSecond
		}

		if (oldIndex == 4 && newIndex == 4) {
			return old.comments.isNotEmpty() && old.comments.isNotEmpty()
		}
		val lastIndexOld = TaskDetailAdapter.ADDITIONAL_CELLS_COUNT + old.comments.size - 1
		val lastIndexNew = TaskDetailAdapter.ADDITIONAL_CELLS_COUNT + new.comments.size - 1
		if (oldIndex == lastIndexOld && newIndex == lastIndexNew) {
			return old.comments.size > 10 && old.comments.size > 10
		}/*

		lastIndexOld = (TaskDetailAdapter.ADDITIONAL_CELLS_COUNT - 2) + old.comments.size
		lastIndexNew = (TaskDetailAdapter.ADDITIONAL_CELLS_COUNT - 2) + new.comments.size
		if (oldIndex == lastIndexOld && newIndex == lastIndexNew) {
			lastIndexOld = oldIndex - TaskDetailAdapter.ADDITIONAL_CELLS_COUNT + 1
			lastIndexNew = newIndex - TaskDetailAdapter.ADDITIONAL_CELLS_COUNT + 1
			return old.comments[lastIndexOld].id == new.comments[lastIndexNew].id
		}
*/
		return false
	}
}