package com.makecity.client.data.temp_problem

import com.makecity.client.data.common.Api
import com.makecity.client.data.geo.GeoPoint
import com.makecity.client.utils.bearer
import com.makecity.core.extenstion.part
import com.squareup.moshi.Json
import io.reactivex.Single
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject


data class CreateTaskRequestBody(
	@Json(name = "latitude") val latitude: Double,
	@Json(name = "longitude") val longitude: Double,
	@Json(name = "text") val text: String,
	@Json(name = "city_id") val cityId: Long,
	@Json(name = "latCity") val latCity: Double,
	@Json(name = "lonCity") val lonCity: Double,
	@Json(name = "org_id") val companyId: Long,
	@Json(name = "category_id") val categoryId: Long,
	@Json(name = "address") val address: String
) {

	fun toMap(): HashMap<String, Any> = hashMapOf(
		"latitude" to latitude.toString().part(),
		"longitude" to longitude.toString().part(),
		"text" to text.part(),
		"city_id" to cityId.toString().part(),
		"latCity" to latCity.toString().part(),
		"lonCity" to lonCity.toString().part(),
		"org_id" to companyId.toString().part(),
		"category_id" to categoryId.toString().part(),
		"address" to address.part()
	)
}


data class CreateTaskRequest(
	val tempProblem: TempProblem,
	val geoPoint: GeoPoint,
	val token: String
)


interface TempTaskService {
	fun createTask(request: CreateTaskRequest): Single<Boolean>
}


class TempTaskServiceRetrofit @Inject constructor(
	private val api: Api
) : TempTaskService {

	override fun createTask(request: CreateTaskRequest) = api.createTask(
		token = bearer(request.token),
		parts = request.run {
			CreateTaskRequestBody(
				latitude = tempProblem.latitude,
				longitude = tempProblem.longitude,
				text = tempProblem.description,
				companyId = tempProblem.companyId,
				categoryId = if (tempProblem.optionId < 0) tempProblem.categoryId else tempProblem.optionId,
				address = tempProblem.address,
				cityId = geoPoint.cityId,
				latCity = geoPoint.latitude,
				lonCity = geoPoint.longitude
			).toMap()
		},
		firstImage = request.tempProblem.images.let {
			if (it.isNotEmpty()) {
				val file = File(it[0])

				val fileReqBody = RequestBody.create(MediaType.parse("image/*"), file)
				MultipartBody.Part.createFormData("img_1", file.name, fileReqBody)
			} else null
		},
		secondImage = request.tempProblem.images.let {
			if (it.size > 1) {
				val file = File(it[1])
				val fileReqBody = RequestBody.create(MediaType.parse("image/*"), file)
				MultipartBody.Part.createFormData("img_2", file.name, fileReqBody)
			} else null
		}
	)
}