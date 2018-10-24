package com.makecity.client.presentation.lists

import android.support.v4.view.ViewCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.makecity.client.R
import com.makecity.client.data.task.Task
import com.makecity.client.data.temp_problem.TempProblem
import com.makecity.client.utils.GoogleApiHelper
import com.makecity.core.data.entity.Location
import com.makecity.core.extenstion.checkNotEmpty
import com.makecity.core.extenstion.isVisible
import com.makecity.core.presentation.list.BaseSingleAdapters
import com.makecity.core.presentation.list.BaseViewHolder
import com.makecity.core.presentation.list.ClickableViewHolder
import com.makecity.core.utils.ScreenUtils
import com.makecity.core.utils.diff.SingleDiffUtil
import com.makecity.core.utils.image.CommonImageRules
import com.makecity.core.utils.image.ImageManager
import kotlinx.android.synthetic.main.item_problem_info.*
import kotlinx.android.synthetic.main.item_problem_location.*
import kotlinx.android.synthetic.main.item_problem_photo.*
import kotlinx.android.synthetic.main.item_problem_preview.*
import kotlinx.android.synthetic.main.item_problem_preview_button.*
import com.makecity.client.R.id.cardView
import android.animation.ObjectAnimator




enum class DataType {
	DESCRIPTION, PHOTO, INFO, LOCATION
}


interface ProblemPerviewDelegate {

	fun onChangeData()

}


class ProblemPreviewAdapter(
	private val imageManager: ImageManager,
	private val problemDelegate: (TempProblem) -> Unit
) : BaseSingleAdapters<TempProblem>(ProblemPreviewDiffUtils()) {

	companion object {
		/**
		 * 0 - content
		 * 1 - location - optional
		 * 2 - info
		 * 3 - photos - optional
		 * 4 - comments title
		 * 5 - show more - optional
		 */
		const val ADDITIONAL_CELLS_COUNT = 5
	}

	override var data: TempProblem? = null

	override fun onCreateViewHolder(
		parent: ViewGroup, type: Int
	): BaseViewHolder<*> = when (type) {
		R.layout.item_problem_preview -> ProblemPreviewViewHolder(
			containerView = LayoutInflater.from(parent.context).inflate(R.layout.item_problem_preview, parent, false),
			itemClickDelegate = problemDelegate,
			imageManager = imageManager
		)

		R.layout.item_problem_location -> LocationPreviewViewHolder(
			containerView = LayoutInflater.from(parent.context).inflate(R.layout.item_problem_location, parent, false),
			imageManager = imageManager
		)

		R.layout.item_problem_info -> ProblemPreviewInfoViewHolder(
			containerView = LayoutInflater.from(parent.context).inflate(R.layout.item_problem_info, parent, false)
		)

		R.layout.item_problem_photo -> ProblemPreviewPhotosViewHolder(
			containerView = LayoutInflater.from(parent.context).inflate(R.layout.item_problem_photo, parent, false),
			imageManager = imageManager
		)

		R.layout.item_problem_preview_button -> ProblemPreviewButtonViewHolder(
			containerView = LayoutInflater.from(parent.context).inflate(R.layout.item_problem_preview_button, parent, false)
		)

		else -> throw IllegalArgumentException("Type with value $type imposable")
	}

	override fun getItemCount(): Int {
		return if (data == null) 0 else ADDITIONAL_CELLS_COUNT
	}

	override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
		when (holder) {
			is LocationPreviewViewHolder -> data?.let {
				holder.bind(it)
			}
			is ProblemPreviewPhotosViewHolder -> data?.let {
				holder.bind(it)
			}
			is ProblemPreviewViewHolder -> data?.let {
				holder.bind(it)
			}
		}
	}

	override fun updateData(data: TempProblem) {
		this.data = data
	}

	override fun getItemViewType(position: Int): Int {
		return when (position) {
			0 -> R.layout.item_problem_preview
			1 -> R.layout.item_problem_info
			2 -> R.layout.item_problem_location
			3 -> R.layout.item_problem_photo
			4 -> R.layout.item_problem_preview_button
			else -> throw IllegalArgumentException("Position with value $position imposable")
		}
	}
}

class ProblemPreviewViewHolder(
	containerView: View,
	override val itemClickDelegate: (TempProblem) -> Unit,
	private val imageManager: ImageManager
) : ClickableViewHolder<TempProblem>(containerView, itemClickDelegate) {

	override lateinit var item: TempProblem

	override fun bind(item: TempProblem) {
		super.bind(item)

		item.apply {
			//problem_preview_item_title.text = optionName
			//problem_preview_item_option.text = categoryName
			problem_preview_item_content.text = description
		}
	}
}

class LocationPreviewViewHolder(
	containerView: View,
	private val imageManager: ImageManager
) : BaseViewHolder<TempProblem>(containerView) {

	override lateinit var item: TempProblem

	override fun bind(item: TempProblem) {
		super.bind(item)

		if (item.latitude == 0.0 && item.longitude == 0.0) {
			containerView.isVisible = false
			containerView.layoutParams = containerView.layoutParams.apply {
				height = 0
			}
			return
		}

		item_problem_location_address.text = item.address
		//problem_location_change.isVisible = true
		val location = Location(item.latitude, item.longitude)
		val rules = CommonImageRules(item_problem_location_image, GoogleApiHelper.createStaticUrl(location))
		imageManager.apply(rules)
	}
}

class ProblemPreviewInfoViewHolder(
	containerView: View
) : BaseViewHolder<TempProblem>(containerView) {

	override lateinit var item: TempProblem
}

class ProblemPreviewPhotosViewHolder(
	containerView: View,
	private val imageManager: ImageManager
) : BaseViewHolder<TempProblem>(containerView) {

	override lateinit var item: TempProblem

	override fun bind(item: TempProblem) {
		super.bind(item)

		//problem_photo_change.isVisible = true

		if (item.imageFirst.isEmpty()) {
			containerView.isVisible = false
			containerView.layoutParams = containerView.layoutParams.apply {
				height = 0
			}
			return
		}

		item.apply {
			imageFirst.checkNotEmpty {
				task_image_first.isVisible = true
				imageManager.apply(CommonImageRules(task_image_first, imageFirst, R.drawable.profile))
			}

			imageSecond.checkNotEmpty {
				task_image_second.isVisible = true
				imageManager.apply(CommonImageRules(task_image_second, imageSecond, R.drawable.profile))
			}
		}
	}
}

class ProblemPreviewButtonViewHolder(
	containerView: View
) : BaseViewHolder<TempProblem>(containerView) {

	override lateinit var item: TempProblem

	init {
		approve_problem_preview.setOnClickListener {

			// val animator = ObjectAnimator.ofFloat(approve_problem_preview, "cardElevation", ScreenUtils.convertDpToPixel(4f),  ScreenUtils.convertDpToPixel(12f))
			// animator.start()
		}
	}
}


class ProblemPreviewDiffUtils: SingleDiffUtil<TempProblem>() {

	override fun areItemsTheSame(oldIndex: Int, newIndex: Int): Boolean {
		return false
	}

	override fun getOldListSize(): Int {
		return 1
	}

	override fun getNewListSize(): Int {
		return 1
	}

	override fun areContentsTheSame(oldIndex: Int, newIndex: Int): Boolean{
		return false
	}
}