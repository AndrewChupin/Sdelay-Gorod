package com.makecity.core.data

import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Transaction


interface BaseDao<Data> {

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insert(partners: Data)

	@Transaction
	fun applyTransaction(closure: () -> Unit) {
		closure()
	}

}
