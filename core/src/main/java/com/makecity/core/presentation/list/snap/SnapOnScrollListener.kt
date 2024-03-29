package com.makecity.core.presentation.list.snap

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SnapHelper
import com.makecity.core.extenstion.getSnapPosition


class SnapOnScrollListener(
	private val snapHelper: SnapHelper,
	var onSnapPositionChangeListener: OnSnapPositionChangeListener? = null,
	var behavior: Behavior = Behavior.NOTIFY_ON_SCROLL
) : RecyclerView.OnScrollListener() {

	enum class Behavior {
		NOTIFY_ON_SCROLL,
		NOTIFY_ON_SCROLL_STATE_IDLE
	}

	private var snapPosition = RecyclerView.NO_POSITION

	override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
		if (behavior == Behavior.NOTIFY_ON_SCROLL) {
			maybeNotifySnapPositionChange(recyclerView)
		}
	}

	override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
		if (behavior == Behavior.NOTIFY_ON_SCROLL_STATE_IDLE
			&& newState == RecyclerView.SCROLL_STATE_IDLE) {
			maybeNotifySnapPositionChange(recyclerView)
		}
	}

	private fun maybeNotifySnapPositionChange(recyclerView: RecyclerView) {
		val snapPosition = snapHelper.getSnapPosition(recyclerView)
		val snapPositionChanged = this.snapPosition != snapPosition
		if (snapPositionChanged) {
			onSnapPositionChangeListener?.onSnapPositionChange(snapPosition)
			this.snapPosition = snapPosition
		}
	}
}