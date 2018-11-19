package com.makecity.client.data.comments

import com.makecity.client.data.auth.AuthDataSource
import com.makecity.client.presentation.auth.AuthData
import com.makecity.core.domain.Mapper
import io.reactivex.Single
import javax.inject.Inject

interface CommentsDataSource {
	fun getComments(page: Int, problemId: Long): Single<List<Comment>>
	fun createComment(text: String, problemId: Long): Single<Boolean>
}

class CommentsDataSourceDefault @Inject constructor(
	private val commentService: CommentService,
	 private val mapperRemote: Mapper<CommentRemote, CommentPersistence>,
	 private val mapperPersistence: Mapper<CommentPersistence, Comment>
): CommentsDataSource {

	override fun getComments(page: Int, problemId: Long): Single<List<Comment>> = Single.defer {
		commentService.loadComments(page, problemId)
			.map(mapperRemote::transformAll)
			.map(mapperPersistence::transformAll)
	}


	override fun createComment(text: String, problemId: Long): Single<Boolean> = Single.defer {
		commentService.requestCreateComment(CreateCommentRequest(
			text, problemId, ""
		))  // TODO
	}
}