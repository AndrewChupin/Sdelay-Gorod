package com.makecity.client.data.task

import com.makecity.client.data.comments.Author
import com.makecity.client.data.comments.AuthorPersistence
import com.makecity.client.data.comments.AuthorRemote
import com.makecity.core.data.Common
import com.makecity.core.data.Dto
import com.makecity.core.data.Persistence
import com.squareup.moshi.Json


// REMOTE MODELS
@Dto
data class TaskRemote(
	@Json(name = "id") val id: Long,
	@Json(name = "title") val title: String?,
	@Json(name = "text") val text: String?,
	@Json(name = "created_at") val createdTime: Long,
	@Json(name = "updated_at") val updatedTime: Long,
	@Json(name = "user_id") val userId: Long,
	@Json(name = "latitude") val latitude: Double,
	@Json(name = "longitude") val longitude: Double,
	@Json(name = "type") val type: String?,
	@Json(name = "img_1") val imageFirst: String?,
	@Json(name = "img_2") val imageSecond: String?,
	@Json(name = "video") val video: String?,
	@Json(name = "status") val status: StatusRemote?,
	@Json(name = "address") val address: String?,
	@Json(name = "compliance") val compliance: Int,
	@Json(name = "count_comments") val commentsCount: Int,
	@Json(name = "count_likes") val likeCounts: Int,
	@Json(name = "city_id") val cityId: Int,
	@Json(name = "author") val author: AuthorRemote,
	@Json(name = "likeIs") val isLiked: Boolean?
)

@Dto
data class StatusRemote(
	@Json(name = "id") val id: Long,
	@Json(name = "name") val name: String?
)


// PERSISTENCE MODELS
@Persistence
data class TaskPersistence(
	val id: Long,
	val title: String,
	val text: String,
	val createdTime: Long,
	val updatedTime: Long,
	val userId: Long,
	val latitude: Double,
	val longitude: Double,
	val type: String,
	val imageFirst: String,
	val imageSecond: String,
	val video: String,
	val status: StatusPersistence,
	val address: String,
	val compliance: Int,
	val commentsCount: Int,
	val likeCounts: Int,
	val cityId: Int,
	val author: AuthorPersistence,
	val isLiked: Boolean
)

@Persistence
data class StatusPersistence(
	val id: Long,
	val name: String
)


// COMMON MODELS
@Common
data class Task(
	val id: Long,
	val status: String,
	val title: String,
	val text: String?,
	val createdTime: Long,
	val updatedTime: Long,
	val latitude: Double,
	val longitude: Double,
	val address: String,
	val commentsCount: Int,
	val likeCounts: Int,
	val imageFirst: String,
	val imageSecond: String,
	val author: Author,
	val isLiked: Boolean
)
