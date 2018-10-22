package com.makecity.client.data.task

import com.makecity.client.data.comments.CommentRemote
import com.makecity.client.data.common.Api
import io.reactivex.Single
import javax.inject.Inject


data class LoadTaskRequest(
	val cityId: Long
)


data class LoadCommentsRequest(
	val problemId: Long
)


interface ProblemService {
	fun requestLoadProblems(request: LoadTaskRequest): Single<List<TaskRemote>>
	fun requestLoadComments(request: LoadCommentsRequest): Single<List<CommentRemote>>
}


class ProblemServiceRetrofit @Inject constructor(
	private val api: Api
): ProblemService {

	override fun requestLoadComments(request: LoadCommentsRequest): Single<List<CommentRemote>>
		= api.loadComments(request.problemId)

	override fun requestLoadProblems(request: LoadTaskRequest)
		= api.loadProblems(request.cityId)
}
