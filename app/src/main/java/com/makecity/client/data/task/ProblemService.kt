package com.makecity.client.data.task

import com.makecity.client.data.comments.CommentRemote
import com.makecity.client.data.common.Api
import io.reactivex.Single
import javax.inject.Inject


data class LoadTaskRequest(
	val cityId: Long,
	val token: String
)


data class LoadCommentsRequest(
	val problemId: Long
)


data class ChangeFavoriteRequest(
	val problemId: Long,
	val favoriteType: FavoriteType
)

enum class FavoriteType {
	LIKE, COMMON
}


interface ProblemService {
	fun requestLoadProblems(request: LoadTaskRequest): Single<List<TaskRemote>>
	fun requestLoadComments(request: LoadCommentsRequest): Single<List<CommentRemote>>
	fun requestChangeFavorite(request: ChangeFavoriteRequest): Single<Boolean>
}


class ProblemServiceRetrofit @Inject constructor(
	private val api: Api
): ProblemService {

	override fun requestLoadComments(request: LoadCommentsRequest): Single<List<CommentRemote>>
		= api.loadComments(request.problemId)

	override fun requestLoadProblems(request: LoadTaskRequest)
		= api.loadProblems("Bearer ${request.token}", request.cityId)


	override fun requestChangeFavorite(
		request: ChangeFavoriteRequest
	): Single<Boolean> = when (request.favoriteType) {
		FavoriteType.LIKE -> api.makeLike(request.problemId)
		FavoriteType.COMMON -> api.removeLike(request.problemId)
	}
}
