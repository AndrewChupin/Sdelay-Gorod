package com.makecity.client.domain.map

import com.makecity.client.data.auth.AuthDataSource
import com.makecity.client.data.comments.Comment
import com.makecity.client.data.comments.CommentsDataSource
import com.makecity.client.data.problem.ProblemDetail
import com.makecity.client.data.task.FavoriteType
import com.makecity.client.data.task.ProblemDataSource
import com.makecity.client.data.task.Task
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject


interface TaskPointsInteractor {
	fun refreshProblems(): Single<List<Task>>
	fun getProblems(): Single<List<Task>>
	fun loadProblemComments(problemId: Long): Single<ProblemDetail>
	fun changeFavorite(problemId: Long, favoriteType: FavoriteType): Single<Boolean>
	fun createComment(problemId: Long, text: String): Completable
	fun getComments(page: Int, problemId: Long): Single<List<Comment>>
}


class TaskInteractorReactive @Inject constructor(
	private val problemDataSource: ProblemDataSource,
	private val commentsDataSource: CommentsDataSource,
	private val authDataSource: AuthDataSource
) : TaskPointsInteractor {

	override fun refreshProblems(): Single<List<Task>> = Single.defer {
		problemDataSource.refreshProblems()
	}

	override fun getProblems(): Single<List<Task>> = Single.defer {
		problemDataSource.getProblems()
	}

	override fun loadProblemComments(problemId: Long): Single<ProblemDetail> = Single.defer {
		problemDataSource.getProblemComments(problemId)
	}

	override fun changeFavorite(
		problemId: Long,
		favoriteType: FavoriteType
	): Single<Boolean> = Single.defer {
		authDataSource
			.checkToken()
			.flatMap { problemDataSource.changeFavorite(problemId, favoriteType) }
	}

	override fun createComment(
		problemId: Long,
		text: String
	): Completable = Completable.defer {
		authDataSource.checkToken()
			.flatMapCompletable { token ->
				problemDataSource.incrementCommentsCount(problemId)
					.andThen(commentsDataSource.createComment(token, text, problemId))
			}
	}

	override fun getComments(
		page: Int,
		problemId: Long
	): Single<List<Comment>> = commentsDataSource.getComments(page, problemId)
}