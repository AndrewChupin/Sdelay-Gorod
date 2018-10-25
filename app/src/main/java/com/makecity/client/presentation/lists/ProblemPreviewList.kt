package com.makecity.client.presentation.lists

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.support.design.animation.AnimatorSetCompat
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.makecity.client.R
import com.makecity.client.data.temp_problem.TempProblem
import com.makecity.client.presentation.create_problem.ProblemPreviewDataType
import com.makecity.client.presentation.lists.ProblemPreviewAdapter.Companion.PROBLEM_PREVIEW_ADDITIONAL_CELLS_COUNT
import com.makecity.client.presentation.views.InfoView
import com.makecity.client.utils.GoogleApiHelper
import com.makecity.core.data.entity.Location
import com.makecity.core.extenstion.checkNotEmpty
import com.makecity.core.extenstion.isVisible
import com.makecity.core.presentation.list.BaseSingleAdapters
import com.makecity.core.presentation.list.BaseViewHolder
import com.makecity.core.utils.ScreenUtils
import com.makecity.core.utils.diff.SingleDiffUtil
import com.makecity.core.utils.image.CommonImageRules
import com.makecity.core.utils.image.ImageManager
import kotlinx.android.synthetic.main.item_problem_info.*
import kotlinx.android.synthetic.main.item_problem_location.*
import kotlinx.android.synthetic.main.item_problem_photo.*
import kotlinx.android.synthetic.main.item_problem_preview.*
import kotlinx.android.synthetic.main.item_problem_preview_button.*


interface ProblemPreviewDelegate {
	fun onApproveCreating(tempProblem: TempProblem)
	fun onChangeData(dataType: ProblemPreviewDataType)
}


class ProblemPreviewAdapter(
	private val imageManager: ImageManager,
	private val delegate: ProblemPreviewDelegate
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
		const val PROBLEM_PREVIEW_ADDITIONAL_CELLS_COUNT = 5
	}

	override var data: TempProblem? = null

	override fun onCreateViewHolder(
		parent: ViewGroup, type: Int
	): BaseViewHolder<*> = when (type) {
		R.layout.item_problem_preview -> ProblemPreviewViewHolder(
			containerView = LayoutInflater.from(parent.context).inflate(R.layout.item_problem_preview, parent, false),
			onChangeContent = delegate::onChangeData,
			imageManager = imageManager
		)

		R.layout.item_problem_location -> LocationPreviewViewHolder(
			containerView = LayoutInflater.from(parent.context).inflate(R.layout.item_problem_location, parent, false),
			onChangeContent = delegate::onChangeData,
			imageManager = imageManager
		)

		R.layout.item_problem_info -> ProblemPreviewInfoViewHolder(
			containerView = LayoutInflater.from(parent.context).inflate(R.layout.item_problem_info, parent, false),
			onChangeContent = delegate::onChangeData
		)

		R.layout.item_problem_photo -> ProblemPreviewPhotosViewHolder(
			containerView = LayoutInflater.from(parent.context).inflate(R.layout.item_problem_photo, parent, false),
			onChangeContent = delegate::onChangeData,
			imageManager = imageManager
		)

		R.layout.item_problem_preview_button -> ProblemPreviewButtonViewHolder(
			containerView = LayoutInflater.from(parent.context).inflate(R.layout.item_problem_preview_button, parent, false),
			onApproveCreating = delegate::onApproveCreating
		)

		else -> throw IllegalArgumentException("Type with value $type imposable")
	}

	override fun getItemCount(): Int {
		return if (data == null) 0 else PROBLEM_PREVIEW_ADDITIONAL_CELLS_COUNT
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
			is ProblemPreviewInfoViewHolder -> data?.let {
				holder.bind(it)
			}
			is ProblemPreviewButtonViewHolder -> data?.let {
				holder.bind(it)
			}
		}
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
	val onChangeContent: (ProblemPreviewDataType) -> Unit,
	private val imageManager: ImageManager
) : BaseViewHolder<TempProblem>(containerView) {

	override lateinit var item: TempProblem

	init {
		problem_preview_description_change.setOnClickListener {
			onChangeContent(ProblemPreviewDataType.DESCRIPTION)
		}
	}

	override fun bind(item: TempProblem) {
		super.bind(item)

		item.apply {
			// TODO problem_preview_item_title.text = optionName
			// problem_preview_item_option.text = categoryName
			problem_preview_item_content.text = description
		}
	}
}

class LocationPreviewViewHolder(
	containerView: View,
	val onChangeContent: (ProblemPreviewDataType) -> Unit,
	private val imageManager: ImageManager
) : BaseViewHolder<TempProblem>(containerView) {

	override lateinit var item: TempProblem

	init {
		item_location_change.setOnClickListener {
			onChangeContent(ProblemPreviewDataType.LOCATION)
		}
	}

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
		item_location_change.isVisible = true
		val location = Location(item.latitude, item.longitude)
		val rules = CommonImageRules(item_problem_location_image, GoogleApiHelper.createStaticUrl(location))
		imageManager.apply(rules)
	}
}

class ProblemPreviewInfoViewHolder(
	containerView: View,
	val onChangeContent: (ProblemPreviewDataType) -> Unit
) : BaseViewHolder<TempProblem>(containerView) {

	override lateinit var item: TempProblem

	init {
		item_info_change.setOnClickListener {
			onChangeContent(ProblemPreviewDataType.INFO)
		}
	}

	override fun bind(item: TempProblem) {
		super.bind(item)

		item_info_change.isVisible = true

		val context = containerView.context

		info_container.run {
			removeAllViews()
			addView(InfoView(context, R.drawable.ic_archive_gray_24dp,
				context.getString(R.string.category), item.categoryName))
			addView(InfoView(context, R.drawable.ic_info_gray_24dp,
				context.getString(R.string.service), item.optionName))
			addView(InfoView(context, R.drawable.ic_group_gray_24dp,
				context.getString(R.string.company), "ЖРП 21")) // TODO
			requestLayout()
		}
	}
}

class ProblemPreviewPhotosViewHolder(
	containerView: View,
	val onChangeContent: (ProblemPreviewDataType) -> Unit,
	private val imageManager: ImageManager
) : BaseViewHolder<TempProblem>(containerView) {

	override lateinit var item: TempProblem

	init {
		item_photos_change.setOnClickListener {
			onChangeContent(ProblemPreviewDataType.PHOTO)
		}
	}

	override fun bind(item: TempProblem) {
		super.bind(item)

		item_photos_change.isVisible = true

		if (item.images.isEmpty()) {
			containerView.isVisible = false
			containerView.layoutParams = containerView.layoutParams.apply {
				height = 0
			}
			return
		}

		item.apply {
			if (images.isNotEmpty()) {
				task_image_first.isVisible = true
				imageManager.apply(CommonImageRules(task_image_first, images.first()))
			}

			if (images.size > 1) {
				task_image_second.isVisible = true
				imageManager.apply(CommonImageRules(task_image_second, images[1]))
			}
		}
	}
}

class ProblemPreviewButtonViewHolder(
	containerView: View,
	val onApproveCreating: (TempProblem) -> Unit
) : BaseViewHolder<TempProblem>(containerView) {

	override lateinit var item: TempProblem

	init {
		val animatorDown = ObjectAnimator.ofFloat(
			approve_problem_preview,
			"cardElevation",
			ScreenUtils.convertDpToPixel(4f),
			ScreenUtils.convertDpToPixel(16f)
		)


		val animatorUp = ObjectAnimator.ofFloat(
			approve_problem_preview,
			"cardElevation",
			ScreenUtils.convertDpToPixel(16f),
			ScreenUtils.convertDpToPixel(4f)
		)

		approve_problem_preview.setOnClickListener {
			onApproveCreating(item)
		}

		approve_problem_preview.setOnTouchListener { view, event ->
			when (event.action) {
				MotionEvent.ACTION_DOWN -> {
					if (animatorUp.isRunning || animatorUp.isStarted) {
						animatorUp.cancel()
					}
					animatorDown.start()
					false
				}
				MotionEvent.ACTION_CANCEL,
				MotionEvent.ACTION_UP -> {
					if (animatorDown.isRunning || animatorDown.isStarted) {
						animatorDown.cancel()
					}
					animatorUp.start()
					false
				}
				else -> false
			}
		}
	}
}


class ProblemPreviewDiffUtils: SingleDiffUtil<TempProblem>() {

	override fun areItemsTheSame(oldIndex: Int, newIndex: Int): Boolean {
		if (itemOld == null || itemNew == null) return false

		return itemOld?.hashCode() == itemNew?.hashCode()
	}

	override fun getOldListSize(): Int = if (itemOld == null) 0 else PROBLEM_PREVIEW_ADDITIONAL_CELLS_COUNT

	override fun getNewListSize(): Int = if (itemNew == null) 0 else PROBLEM_PREVIEW_ADDITIONAL_CELLS_COUNT

	override fun areContentsTheSame(oldIndex: Int, newIndex: Int): Boolean {
		if (itemOld == null || itemNew == null) return false

		return itemOld == itemNew
	}
}