package com.makecity.client.data.profile

import com.makecity.client.data.common.Api
import io.reactivex.Single
import javax.inject.Inject


interface ProfileService {
	fun loadProfile(token: String): Single<ProfileRemote>
}


class ProfileServiceRetrofit @Inject constructor(
	private val api: Api
): ProfileService {

	override fun loadProfile(token: String): Single<ProfileRemote> =
		api.getProfile("Bearer $token")

}