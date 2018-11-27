package com.makecity.client.data.comments

import com.makecity.client.data.common.Api
import io.reactivex.Single
import javax.inject.Inject

data class CreateCommentRequest(
	val text: String,
	val problemId: Long
)


interface CommentService {
	fun loadComments(page: Int, problemId: Long): Single<List<CommentRemote>>
	fun requestCreateComment(request: CreateCommentRequest): Single<Boolean>
}


class CommentServiceRetrofit @Inject constructor(
	private val api: Api
): CommentService {

	override fun loadComments(page: Int, problemId: Long): Single<List<CommentRemote>>
		= api.loadCommentsPage(page, problemId)

	override fun requestCreateComment(request: CreateCommentRequest): Single<Boolean> = api.createComment(request.text, request.problemId)

}