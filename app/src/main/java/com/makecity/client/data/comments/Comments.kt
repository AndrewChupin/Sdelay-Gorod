package com.makecity.client.data.comments

import com.makecity.core.data.Common
import com.makecity.core.data.Dto
import com.makecity.core.data.Persistence
import com.squareup.moshi.Json


// REMOTE MODELS
@Dto
data class CommentRemote(
	@Json(name = "id") val id: Long,
	@Json(name = "text") val text: String?,
	@Json(name = "created_at") val createdTime: Long,
	@Json(name = "updated_at") val updatedTime: Long,
	@Json(name = "user_id") val userId: Long,
	@Json(name = "problem_id") val problemId: Long,
	@Json(name = "name_guest") val nameGuest: String?,
	@Json(name = "status") val status: Int,
	@Json(name = "author") val author: AuthorRemote
)


@Dto
data class AuthorRemote(
	@Json(name = "username") val userName: String?,
	@Json(name = "role") val role: String?,
	@Json(name = "phone") val phone: String?,
	@Json(name = "foto") val image: String?
)


// PERSISTENCE MODELS
@Persistence
data class CommentPersistence(
	val id: Long,
	val text: String,
	val createdTime: Long,
	val updatedTime: Long,
	val userId: Long,
	val problemId: Long,
	val nameGuest: String,
	val status: Int,
	val author: AuthorPersistence
)

@Persistence
data class AuthorPersistence(
	val userName: String,
	val role: String,
	val phone: String,
	val image: String
)


// COMMON MODELS
@Common
data class Comment(
	val id: Long,
	val text: String,
	val createdTime: Long,
	val updatedTime: Long,
	val userId: Long,
	val problemId: Long,
	val nameGuest: String,
	val status: Int,
	val author: Author
)

@Common
data class Author(
	val userName: String,
	val role: String,
	val phone: String,
	val image: String
)
