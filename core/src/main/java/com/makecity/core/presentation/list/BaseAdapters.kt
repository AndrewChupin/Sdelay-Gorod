package com.makecity.core.presentation.list

import android.support.annotation.CallSuper
import android.support.v7.widget.RecyclerView
import com.makecity.core.utils.diff.BaseDiffUtil
import com.makecity.core.utils.diff.SingleDiffUtil


interface DiffAdapter<Data> {
	val diffUtilCallback: SingleDiffUtil<Data>
}

interface DataAdapter<Data> {
	fun updateData(data: Data)
}


abstract class BaseMultiplyAdapter<Data, VH : BaseViewHolder<Data>>(
	override val diffUtilCallback: SingleDiffUtil<List<Data>> = BaseDiffUtil()
) : RecyclerView.Adapter<VH>(), DiffAdapter<List<Data>>, DataAdapter<List<Data>> {
	abstract var data: List<Data>

	@CallSuper
	override fun updateData(data: List<Data>) {
		this.data = data
	}
}


abstract class BaseSingleAdapters<Data>(
	override val diffUtilCallback: SingleDiffUtil<Data>
) : RecyclerView.Adapter<BaseViewHolder<*>>(), DiffAdapter<Data>, DataAdapter<Data> {
	abstract var data: Data?

	@CallSuper
	override fun updateData(data: Data) {
		this.data = data
	}
}

