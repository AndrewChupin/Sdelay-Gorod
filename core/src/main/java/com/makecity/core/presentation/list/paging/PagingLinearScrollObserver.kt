package com.makecity.core.presentation.list.paging

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import java.lang.ref.WeakReference
import javax.inject.Inject


class PagingLinearScrollObserver @Inject constructor(
	private var layoutManager: LinearLayoutManager
): RecyclerPagingScrollObserver() {

	private var adapters: MutableList<WeakReference<PagingActionsAdapter>> = mutableListOf()

	override fun addObserver(actionsAdapter: PagingActionsAdapter) {
		adapters.add(WeakReference(actionsAdapter))
		if (layoutManager.itemCount == 0) {
			actionsAdapter.initFirstPage()
		}
	}

	override fun removeObserver(actionsAdapter: PagingActionsAdapter) {
		adapters.remove(WeakReference(actionsAdapter))
	}

	override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
		super.onScrolled(recyclerView, dx, dy)

		val visibleFirstPosition = layoutManager.findFirstVisibleItemPosition()
		val visibleLastPosition = layoutManager.findLastVisibleItemPosition()
		adapters.forEach {
			it.get()?.onPageChangedPosition(visibleFirstPosition, visibleLastPosition)
		}
	}

}
