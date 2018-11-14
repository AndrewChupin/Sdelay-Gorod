package com.makecity.client.data.comments

import com.makecity.core.domain.Mapper
import io.reactivex.Single
import javax.inject.Inject

interface CommentsDataSource {
	fun getComments(page: Int): Single<List<Comment>>
}

class CommentsDataSourceDefault @Inject constructor(
	private val commentService: CommentService,
	 private val mapperRemote: Mapper<CommentRemote, CommentPersistence>,
	 private val mapperPersistence: Mapper<CommentPersistence, Comment>
): CommentsDataSource {

	override fun getComments(page: Int): Single<List<Comment>> = Single.defer {
		commentService.loadComments(page)
			.map(mapperRemote::transformAll)
			.map(mapperPersistence::transformAll)
	}
}