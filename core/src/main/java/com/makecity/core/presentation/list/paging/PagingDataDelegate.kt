package com.makecity.core.presentation.list.paging


data class PagingState(
	val currentPosition: Int,
	val currentSize: Int,
	val nextPageSize: Int,
	val pageCounts: Int
)


interface PagingDataDelegate {
	fun onLoadPage(state: PagingState, result: (Int) -> Unit)
}
