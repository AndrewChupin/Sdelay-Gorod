package com.makecity.core.presentation.list.paging

import android.support.annotation.MainThread
import javax.inject.Inject


class PagingActionsAdapterDefault @Inject constructor(
	private val pagingConfig: PagingConfig
) : PagingActionsAdapter {

	override var pagingDataActions: PagingDataDelegate? = null

	private var isLoading = false
	private var isLastPage = false

	private var pageCounts: Int = pagingConfig.initialPage
	private var currentSize: Int = pagingConfig.initialSize
	override lateinit var state: PagingState

	@MainThread
	override fun initFirstPage() {
		if (isLoading || isLastPage) {
			return
		}

		state = PagingState(
			currentPosition = 0,
			currentSize = currentSize,
			nextPageSize = pagingConfig.firstPageSize,
			pageCounts = pageCounts
		)

		load(state)
	}

	@MainThread
	override fun onPageChangedPosition(firstVisible: Int, lastVisible: Int) {
		if (isLoading || isLastPage) {
			return
		}

		val bound = currentSize - lastVisible
		if (bound < pagingConfig.loadingBound) {

			state = PagingState(
				currentPosition = 0,
				currentSize = currentSize,
				nextPageSize = pagingConfig.firstPageSize,
				pageCounts = pageCounts
			)

			load(state)
		}
	}

	private fun load(state: PagingState) {
		isLoading = true
		pagingDataActions?.onLoadPage(state, ::onPageDidLoad)
	}

	@MainThread
	private fun onPageDidLoad(pageSize: Int) {
		if (pageSize > 0) {
			currentSize += pageSize
			pageCounts++
		} else {
			isLastPage = true
		}

		isLoading = false
	}
}
