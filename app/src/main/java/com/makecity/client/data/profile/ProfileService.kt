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
	@Json(name = "street") val street: String,
	@Json(name = "sex") val sex: String
) {

	fun toMap(): HashMap<String, Any> = hashMapOf(
		"first_name" to firstName,
		"street" to street,
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
				sex = profile.sex,
				street = profile.street
			).toMap(),

			photo = if (profile.photo.isNotEmpty() && !profile.photo.startsWith("http")) { // TODO LATE change startsWith
				val file = File(profile.photo)
				val fileReqBody = RequestBody.create(MediaType.parse("image/*"), file)
				MultipartBody.Part.createFormData("photo", file.name, fileReqBody)
			} else null
		)
}