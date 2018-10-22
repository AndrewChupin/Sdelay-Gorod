package com.makecity.client.data.category

import com.makecity.core.data.Common
import com.makecity.core.data.Dto
import com.makecity.core.data.Persistence
import com.squareup.moshi.Json


@Dto
data class CategoryRemote(
	@Json(name = "id") val id: Long,
	@Json(name = "name") val name: String?,
	@Json(name = "sub") val options: List<OptionRemote>?
)

@Dto
data class OptionRemote(
	@Json(name = "id") val id: Long,
	@Json(name = "name") val name: String?
)


@Persistence
data class CategoryPersistence(
	val id: Long,
	val name: String,
	val options: List<OptionPersistence>
)

@Persistence
data class OptionPersistence(
	val id: Long,
	val name: String
)


@Common
data class Category(
	val id: Long,
	val name: String,
	val options: List<Option>
)

@Common
data class Option(
	val id: Long,
	val name: String
)