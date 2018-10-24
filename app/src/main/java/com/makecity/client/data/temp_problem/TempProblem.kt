package com.makecity.client.data.temp_problem

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.makecity.core.data.Common
import com.makecity.core.data.Persistence
import com.makecity.core.utils.Symbols.EMPTY


@Persistence
@Entity(tableName = "temp_problem")
data class TempProblemPersistence(
	@PrimaryKey(autoGenerate = true) val id: Long,
	@ColumnInfo(name = "category_id") val categoryId: Long?  = null,
	@ColumnInfo(name = "option_id") val optionId: Long? = null,
	@ColumnInfo(name = "category_name") val categoryName: String = EMPTY,
	@ColumnInfo(name = "option_name") val optionName: String = EMPTY,
	@ColumnInfo(name = "image_first") val imageFirst: String = EMPTY,
	@ColumnInfo(name = "image_second") val imageSecond: String = EMPTY,
	@ColumnInfo(name = "description") val description: String = EMPTY,
	@ColumnInfo(name = "latitude") val latitude: Double = 0.0,
	@ColumnInfo(name = "longitude") val longitude: Double = 0.0,
	@ColumnInfo(name = "address") val address: String = EMPTY
)


@Common
data class TempProblem(
	val id: Long = 0,
	val categoryId: Long? = null,
	val categoryName: String = EMPTY,
	val optionName: String = EMPTY,
	val optionId: Long? = null,
	val imageFirst: String = EMPTY,
	val imageSecond: String = EMPTY,
	val description: String = EMPTY,
	val latitude: Double = 0.0,
	val longitude: Double = 0.0,
	val address: String = EMPTY
)