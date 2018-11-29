package com.makecity.client.data.profile

import com.makecity.client.data.common.Api
import com.squareup.moshi.Json
import io.reactivex.Single
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject


data class SaveProfileRequest(
	@Json(name = "first_name") val firstName: String,
	@Json(name = "last_name") val lastName: String,
	@Json(name = "street") val street: String,
	@Json(name = "house") val house: String,
	@Json(name = "sex") val sex: String
) {

	fun toMap() = hashMapOf(
		"first_name" to firstName,
		"last_name" to lastName,
		"street" to street,
		"house" to house,
		"sex" to sex
	)

}


interface ProfileService {
	fun loadProfile(token: String): Single<ProfileRemote>
	fun saveProfile(token: String, profile: Profile): Single<Boolean>
}


class ProfileServiceRetrofit @Inject constructor(
	private val api: Api
): ProfileService {

	override fun loadProfile(token: String): Single<ProfileRemote> =
		api.getProfile("Bearer $token")

	override fun saveProfile(token: String, profile: Profile): Single<Boolean> =
		api.saveProfile(
			token = "Bearer $token",

			parts = SaveProfileRequest(
				firstName = profile.firstName,
				lastName = profile.lastName,
				sex = profile.sex,
				street = profile.street,
				house = profile.house
			).toMap(),

			photo = if (profile.photo.isNotEmpty() && !profile.photo.startsWith("http")) { // TODO LATE change startsWith
				val file = File(profile.photo)
				val fileReqBody = RequestBody.create(MediaType.parse("image/*"), file)
				MultipartBody.Part.createFormData("photo", file.name, fileReqBody)
			} else null
		)
}