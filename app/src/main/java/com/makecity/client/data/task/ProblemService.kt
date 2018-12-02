package com.makecity.client.data.task

import com.makecity.client.data.comments.CommentRemote
import com.makecity.client.data.common.Api
import com.makecity.client.utils.bearer
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
	val favoriteType: FavoriteType,
	val token: String
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
) : ProblemService {

	override fun requestLoadComments(request: LoadCommentsRequest): Single<List<CommentRemote>> = api.loadComments(request.problemId)

	override fun requestLoadProblems(request: LoadTaskRequest) = api.loadProblems(bearer(request.token), request.cityId)


	override fun requestChangeFavorite(
		request: ChangeFavoriteRequest
	): Single<Boolean> = when (request.favoriteType) {
		FavoriteType.LIKE -> api.makeLike(bearer(request.token), request.problemId)
		FavoriteType.COMMON -> api.removeLike(bearer(request.token), request.problemId)
	}
}


