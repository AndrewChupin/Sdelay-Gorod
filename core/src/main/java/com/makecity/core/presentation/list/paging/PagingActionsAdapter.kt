package com.makecity.core.presentation.list.paging

import android.support.annotation.MainThread


data class PagingConfig(
	val firstPageSize: Int,
	val commonPageSize: Int,
	val loadingBound: Int,
	val initialPage: Int = 0,
	val initialSize: Int = 0
)


interface PagingActionsAdapter {
	var pagingDataActions: PagingDataDelegate?

	@MainThread
	fun initFirstPage()

	@MainThread
	fun onPageChangedPosition(firstVisible: Int, lastVisible: Int) { /* Default implementation */ }

	@MainThread
	fun onPageChangedScroll(dY: Int, dX: Float) { /* Default implementation */ }
}
