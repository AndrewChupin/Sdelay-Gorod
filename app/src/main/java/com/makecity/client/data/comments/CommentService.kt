package com.makecity.client.data.comments

import com.makecity.client.data.common.Api
import io.reactivex.Single
import javax.inject.Inject

interface CommentService {
	fun loadComments(page: Int): Single<List<CommentRemote>>
}


class CommentServiceRetrofit @Inject constructor(
	private val api: Api
): CommentService {

	override fun loadComments(page: Int): Single<List<CommentRemote>> = api.loadComments(page)

}