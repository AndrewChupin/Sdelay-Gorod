package com.makecity.client.data.comments

import com.makecity.client.data.common.Api
import com.makecity.client.utils.bearer
import com.squareup.moshi.Json
import io.reactivex.Single
import javax.inject.Inject

data class CreateCommentRequest(
	@Json(name = "text") val text: String,
	@Json(name = "problem_id") val problemId: Long
)


interface CommentService {
	fun loadComments(page: Int, problemId: Long): Single<List<CommentRemote>>
	fun requestCreateComment(auth: String, request: CreateCommentRequest): Single<CommentRemote>
}


class CommentServiceRetrofit @Inject constructor(
	private val api: Api
) : CommentService {

	override fun loadComments(
		page: Int, problemId: Long
	): Single<List<CommentRemote>> = api.loadCommentsPage(page, problemId)

	override fun requestCreateComment(
		auth: String, request: CreateCommentRequest
	): Single<CommentRemote> = api.createComment(bearer(auth), request)

}