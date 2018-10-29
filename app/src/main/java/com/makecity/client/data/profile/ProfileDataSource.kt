package com.makecity.client.data.profile

import com.makecity.client.data.auth.AuthStorage
import com.makecity.client.data.auth.TokenNotFounded
import com.makecity.core.domain.Mapper
import com.makecity.core.extenstion.blockingCompletable
import io.reactivex.Single
import javax.inject.Inject


interface ProfileDataSource {
	fun getProfile(): Single<Profile>
}


class ProfileDataSourceDefault @Inject constructor(
	private val profileService: ProfileService,
	private val profileStorage: ProfileStorage,
	private val authStorage: AuthStorage,
	private val mapperRemote: Mapper<ProfileRemote, ProfilePersistence>,
	private val mapperCommon: Mapper<ProfilePersistence, Profile>
): ProfileDataSource {

	override fun getProfile(): Single<Profile> = Single.defer {
		authStorage
			.getAuthToken()
			.map { if (it.isEmpty()) throw TokenNotFounded else it  }
			.flatMap(profileService::loadProfile)
			.map(mapperRemote::transform)
			.blockingCompletable(profileStorage::saveProfile)
			.map(mapperCommon::transform)
	}
}
