package com.makecity.client.data.common

import com.makecity.client.data.auth.*
import com.makecity.client.data.category.CategoryRemote
import com.makecity.client.data.comments.CommentRemote
import com.makecity.client.data.geo.GeoPointRemote
import com.makecity.client.data.task.TaskRemote
import io.reactivex.Single
import retrofit2.http.*


interface Api {

	/**
	 * MARK - Category
	 */
	@GET("category")
	fun loadCategories(): Single<List<CategoryRemote>>


	/**
	 * MARK - Problems
	 */
	@GET("problem?expand=likeIs,org,count_likes,history,author")
	fun loadProblems(
		@Query("pm[city_id]") cityId: Long
	): Single<List<TaskRemote>>

	@GET("comment/problem/{problem_id}?page=1&expand=likeIs,org,count_likes,history,author")
	fun loadComments(
		@Path("problem_id") problemId: Long
	): Single<List<CommentRemote>>


	/**
	 * MARK - Likes
	 */
	@GET("problem/{problem_id}/remove-like")
	fun removeLike(
		@Path("problem_id") problemId: Long
	): Single<List<TaskRemote>>

	@GET("problem/<problem_id>/put-like")
	fun makeLike(
		@Path("problem_id") problemId: Long
	): Single<List<TaskRemote>>


	/**
	 * MARK - Auth
	 */
	@POST("auth/phone-auth")
	fun sendPhone(
		@Body requestBody: GetSmsRequestBody
	): Single<NextStepResponse>

	@POST("auth/confirm-phone")
	fun confirmPhone(
		@Body requestBody: CheckSmsRequestBody
	): Single<RegistrationTokenResponse>

	@POST("auth/set-pass")
	fun setPassword(
		@Body body: CreatePasswordRequestBody
	): Single<AuthTokenResponse>

	@POST("auth")
	fun checkPassword(
		@Body body: CheckPasswordRequestBody
	): Single<AuthTokenResponse>


	/**
	 * MARK - Geo
	 */
	@GET("geolocator/ip/get-ip")
	fun getIp(): Single<String>

	@GET("/geolocator/ip/get-client-ip")
	fun getCity(
		@Query("ip") ip: String
	): Single<GeoPointRemote>
}