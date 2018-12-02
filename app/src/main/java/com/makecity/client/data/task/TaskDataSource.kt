package com.makecity.client.data.task

import com.makecity.client.data.auth.AuthDataSource
import com.makecity.client.data.auth.TokenNotFounded
import com.makecity.client.data.comments.Comment
import com.makecity.client.data.comments.CommentsAuthorMapperDtoToPersistence
import com.makecity.client.data.comments.CommentsAuthorMapperPersistenceToCommon
import com.makecity.client.data.geo.GeoDataSource
import com.makecity.client.data.problem.ProblemDetail
import com.makecity.core.utils.Symbols.EMPTY
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import javax.inject.Inject


interface ProblemDataSource {
	fun getProblems(): Single<List<Task>>
	fun refreshProblems(): Single<List<Task>>
	fun getProblemComments(problemId: Long): Single<ProblemDetail>
	fun changeFavorite(problemId: Long, favoriteType: FavoriteType): Single<Boolean>
}

class ProblemDataSourceRemote @Inject constructor(
	private val problemService: ProblemService,
	private val geoDataSource: GeoDataSource,
	private val authDataSource: AuthDataSource,
	private val mapperProblemDto: ProblemMapperDtoToPersistence,
	private val mapperProblemPersist: ProblemMapperPersistenceToCommon,
	private val mapperCommentsDto: CommentsAuthorMapperDtoToPersistence,
	private val mapperCommentsPersist: CommentsAuthorMapperPersistenceToCommon
) : ProblemDataSource {

	private var tasks: List<Task> = emptyList()

	override fun getProblems(): Single<List<Task>> = Single.defer {
		if (tasks.isEmpty()) {
			refreshProblems()
		} else {
			Single.fromCallable { tasks }
		}
	}

	override fun refreshProblems(): Single<List<Task>> = geoDataSource
		.getDefaultGeoPoint()
		.flatMapSingle { point ->
			authDataSource
				.checkToken()
				.onErrorResumeNext {
					if (it is TokenNotFounded) {
						Single.just(EMPTY)
					} else {
						Single.error(it)
					}
				}
				.flatMap {
					problemService.requestLoadProblems(
						LoadTaskRequest(point.cityId, it)
					)
				}
		}
		.map(mapperProblemDto::transformAll)
		.map(mapperProblemPersist::transformAll)
		.doOnSuccess { tasks = it }

	override fun getProblemComments(problemId: Long): Single<ProblemDetail> = Single.defer {
		val task = Single.just(tasks.find { it.id == problemId }!!) // TODO LATE
		Single.zip(getComments(problemId), task, BiFunction<List<Comment>, Task, ProblemDetail> { t1, t2 ->
			ProblemDetail(t2, t1)
		})
	}

	private fun getComments(problemId: Long): Single<List<Comment>> = Single.defer {
		problemService.requestLoadComments(LoadCommentsRequest(problemId))
			.map(mapperCommentsDto::transformAll)
			.map(mapperCommentsPersist::transformAll)
	}

	override fun changeFavorite(
		problemId: Long,
		favoriteType: FavoriteType
	): Single<Boolean> = Single.defer {
		authDataSource.checkToken()
			.flatMap {
				problemService.requestChangeFavorite(
					ChangeFavoriteRequest(
						problemId = problemId,
						favoriteType = favoriteType,
						token = it
					)
				)
			}
			.doOnSuccess { _ ->
				tasks = tasks.map {
					if (it.id == problemId) it.copy(isLiked = !it.isLiked)
					else it
				}
			}
	}
}