package com.makecity.core.extenstion

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import com.makecity.core.presentation.list.DataAdapter
import com.makecity.core.presentation.list.DiffAdapter


inline fun <reified Data, Adapter> Adapter.calculateDiffs(
    new: Data,
    detectMoves: Boolean = false
) where Adapter : RecyclerView.Adapter<*>,
        Adapter : DiffAdapter<Data>,
        Adapter : DataAdapter<Data> {
    diffUtilCallback.updateItem(new)
    val result = DiffUtil.calculateDiff(diffUtilCallback, detectMoves)
    updateData(new)
    result.dispatchUpdatesTo(this)
}
