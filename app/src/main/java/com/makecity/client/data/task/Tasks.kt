package com.makecity.client.data.task

import com.makecity.client.data.comments.Author
import com.makecity.client.data.comments.AuthorPersistence
import com.makecity.client.data.comments.AuthorRemote
import com.makecity.client.data.company.CompanyRemote
import com.makecity.core.data.Common
import com.makecity.core.data.Dto
import com.makecity.core.data.Persistence
import com.makecity.core.data.entity.Identical
import com.squareup.moshi.Json

enum class ProblemStatus {
	NEW, IN_PROGRESS, DONE, CANCELED, REJECT
}


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
	@Json(name = "likeIs") val isLiked: Boolean?,
	@Json(name = "category") val categories: TaskCategoriesRemote,
	@Json(name = "orgs") val companies: List<CompanyRemote>?,
	@Json(name = "history") val history: List<HistoryRemote>?
)

@Dto
data class StatusRemote(
	@Json(name = "id") val id: Long,
	@Json(name = "name") val name: String?
)

@Dto
data class TaskCategoriesRemote(
	@Json(name = "main") val main: TaskCategoryRemote?,
	@Json(name = "sub") val sub: TaskCategoryRemote?
)

@Dto
data class TaskCategoryRemote(
	@Json(name = "id") val id: Long,
	@Json(name = "name") val name: String
)

@Dto
data class HistoryRemote(
	@Json(name = "id") val id: Long,
	@Json(name = "text") val text: String?,
	@Json(name = "img_1") val imageFirst: String?,
	@Json(name = "img_2") val imageSecond: String?,
	@Json(name = "created_at") val createTime: Long,
	@Json(name = "updated_at") val updateTime: Long,
	@Json(name = "problem_id") val problemId: Long,
	@Json(name = "history") val history: List<HistoryPersistence>
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
	val isLiked: Boolean,
	val categories: TaskCategoriesPersistence,
	val companyName: String,
	val history: List<HistoryPersistence>
)


@Persistence
data class StatusPersistence(
	val id: Long,
	val name: String
)

@Persistence
data class TaskCategoriesPersistence(
	val main: TaskCategoryPersistence,
	val sub: TaskCategoryPersistence?
)

@Persistence
data class TaskCategoryPersistence(
	val id: Long,
	val name: String
)

@Persistence
data class HistoryPersistence(
	val id: Long,
	val text: String,
	val imageFirst: String,
	val imageSecond: String,
	val createTime: Long,
	val updateTime: Long,
	val problemId: Long
)

// COMMON MODELS
@Common
data class Task(
	override val id: Long,
	val status: String,
	val title: String,
	val text: String?,
	val createdTime: Long,
	val updatedTime: Long,
	val statusType: ProblemStatus,
	val latitude: Double,
	val longitude: Double,
	val address: String,
	val commentsCount: Int,
	val likeCounts: Int,
	val imageFirst: String,
	val imageSecond: String,
	val author: Author,
	val isLiked: Boolean,
	val categories: TaskCategories,
	val companyName: String,
	val history: List<History>
) : Identical


@Common
data class TaskCategories(
	val main: TaskCategory,
	val sub: TaskCategory?
)

@Common
data class TaskCategory(
	val id: Long,
	val name: String
)

@Persistence
data class History(
	val id: Long,
	val text: String,
	val imageFirst: String,
	val imageSecond: String,
	val createTime: Long,
	val updateTime: Long,
	val problemId: Long
)
