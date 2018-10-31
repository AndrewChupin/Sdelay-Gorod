package com.makecity.client.data.common

import com.makecity.client.data.auth.*
import com.makecity.client.data.category.CategoryRemote
import com.makecity.client.data.comments.CommentRemote
import com.makecity.client.data.company.CompanyRemote
import com.makecity.client.data.geo.GeoPointRemote
import com.makecity.client.data.profile.ProfileRemote
import com.makecity.client.data.task.TaskRemote
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.http.*


interface Api {

	/**
	 * MARK - Company
	 */
	@GET("profiles/organization/get-all-organizations")
	fun loadCompanies(): Single<List<CompanyRemote>>

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

	@Multipart
	@POST("problem")
	fun createTask(
		@Header("Authorization") token: String,
		@PartMap parts: HashMap<String, Any>,
		@Part firstImage: MultipartBody.Part? = null,
		@Part secondImage: MultipartBody.Part? = null
	): Single<Boolean>


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

	@POST("auth/get-reg-token")
	fun confirmPhone(
		@Body requestBody: CheckSmsRequestBody
	): Single<RegistrationTokenResponse>

	@POST("/auth/request-new-sms-code")
	fun refreshSms(
		@Body requestBody: NewSmsRequestBody
	): Single<Boolean>

	@POST("auth/set-pass")
	fun setPassword(
		@Body body: CreatePasswordRequestBody
	): Single<AuthTokenResponse>

	@POST("auth")
	fun checkPassword(
		@Body body: CheckPasswordRequestBody
	): Single<AuthTokenResponse>


	/**
	 * MARK - Profile
	 */
	@GET("/profiles/user/view?expand=user")
	fun getProfile(
		@Header("Authorization") token: String
	): Single<ProfileRemote>


	/**
	 * MARK - Geo
	 */
	@GET("geolocator/ip/get-ip")
	fun getIp(): Single<String>

	@GET("geolocator/ip/get-client-ip")
	fun getCity(
		@Query("ip") ip: String
	): Single<GeoPointRemote>
}