package com.makecity.core.utils.diff

import android.support.annotation.MainThread
import android.support.v7.util.DiffUtil


abstract class SingleDiffUtil<Data>: DiffUtil.Callback() {

	protected var itemOld: Data? = null
	protected var itemNew: Data? = null

	@MainThread
	fun updateItem(newItem: Data) {
		this.itemOld = this.itemNew
		this.itemNew = newItem
	}
}