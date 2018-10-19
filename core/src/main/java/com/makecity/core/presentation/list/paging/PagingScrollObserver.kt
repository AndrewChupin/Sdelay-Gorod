package com.makecity.core.presentation.list.paging

import android.support.v7.widget.RecyclerView


interface PagingScrollObserver {
	fun addObserver(actionsAdapter: PagingActionsAdapter)
	fun removeObserver(actionsAdapter: PagingActionsAdapter)
}


abstract class RecyclerPagingScrollObserver: RecyclerView.OnScrollListener(), PagingScrollObserver
