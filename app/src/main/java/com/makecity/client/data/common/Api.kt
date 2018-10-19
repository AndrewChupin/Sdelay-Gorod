package com.makecity.client.data.common

import com.makecity.client.data.comments.CommentRemote
import com.makecity.client.data.task.TaskRemote
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface Api {

	@GET("problem?expand=likeIs,org,count_likes,history,author")
	fun loadProblems(
		@Query("pm[city_id]") cityId: Long
	): Single<List<TaskRemote>>

	@GET("comment/problem/{problem_id}?page=1&expand=likeIs,org,count_likes,history,author")
	fun loadComments(
		@Path("problem_id") problemId: Long
	): Single<List<CommentRemote>>

	@GET("problem/{problem_id}/remove-like")
	fun removeLike(
		@Path("problem_id") problemId: Long
	): Single<List<TaskRemote>>

	@GET("problem/<problem_id>/put-like")
	fun makeLike(
		@Path("problem_id") problemId: Long
	): Single<List<TaskRemote>>

}