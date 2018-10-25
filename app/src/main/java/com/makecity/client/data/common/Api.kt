package com.makecity.client.data.common

import com.makecity.client.data.category.CategoryRemote
import com.makecity.client.data.comments.CommentRemote
import com.makecity.client.data.geo.GeoPointRemote
import com.makecity.client.data.task.TaskRemote
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query


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
	@GET("auth/phone-auth")
	fun sendPhone(
		@Query("city_id") cityId: Long,
		@Query("phone") phone: String
	): Single<String>

	@GET("auth/confirm-phone")
	fun confirmPhone(
		@Header("Authorization") token: String,
		@Query("code") code: String
	): Single<String>

	@GET("auth/set-pass")
	fun setPassword(
		@Header("Authorization") token: String,
		@Query("pass") phone: String
	): Single<String>


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