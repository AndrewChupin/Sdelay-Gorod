package com.makecity.client.data.temp_problem

import android.arch.persistence.room.*
import com.makecity.core.data.Common
import com.makecity.core.data.Persistence
import com.makecity.core.utils.Symbols.EMPTY

class ImagesPathsConverter {

	@TypeConverter
	fun fromImages(images: List<String>): String = when (images.size) {
		0 -> EMPTY
		1 -> images[0]
		else -> images.joinToString("||") // TODO LATE
	}

	@TypeConverter
	fun toImages(data: String): List<String> = if (data.isEmpty()) emptyList() else data.split("||")

}

@Persistence
@Entity(tableName = "temp_problem")
@TypeConverters(ImagesPathsConverter::class)
data class TempProblemPersistence(
	@PrimaryKey(autoGenerate = true) val id: Long,
	@ColumnInfo(name = "category_id") val categoryId: Long? = null,
	@ColumnInfo(name = "option_id") val optionId: Long? = null,
	@ColumnInfo(name = "category_name") val categoryName: String = EMPTY,
	@ColumnInfo(name = "option_name") val optionName: String = EMPTY,
	@ColumnInfo(name = "company_id") val companyId: Long? = null,
	@ColumnInfo(name = "company_name") val companyName: String = EMPTY,
	@ColumnInfo(name = "images") val images: List<String> = emptyList(),
	@ColumnInfo(name = "description") val description: String = EMPTY,
	@ColumnInfo(name = "latitude") val latitude: Double = 0.0,
	@ColumnInfo(name = "longitude") val longitude: Double = 0.0,
	@ColumnInfo(name = "address") val address: String = EMPTY
)


@Common
data class TempProblem(
	val id: Long = -1,
	val categoryId: Long = -1,
	val categoryName: String = EMPTY,
	val optionName: String = EMPTY,
	val companyId: Long = -1,
	val companyName: String = EMPTY,
	val optionId: Long = -1,
	val images: List<String> = emptyList(),
	val description: String = EMPTY,
	val latitude: Double = 0.0,
	val longitude: Double = 0.0,
	val address: String = EMPTY
)