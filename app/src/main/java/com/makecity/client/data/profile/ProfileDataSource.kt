package com.makecity.client.data.profile

import com.makecity.client.data.auth.AuthStorage
import com.makecity.client.data.auth.TokenNotFounded
import com.makecity.core.domain.Mapper
import com.makecity.core.extenstion.blockingCompletable
import com.makecity.core.utils.Symbols.EMPTY
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.internal.operators.maybe.MaybeDefer
import javax.inject.Inject


interface ProfileDataSource {
	fun refreshProfile(): Single<Profile>
	fun getProfile(): Maybe<Profile>
	fun deleteProfile(): Completable
}


class ProfileDataSourceDefault @Inject constructor(
	private val profileService: ProfileService,
	private val profileStorage: ProfileStorage,
	private val authStorage: AuthStorage,
	private val mapperRemote: Mapper<ProfileRemote, ProfilePersistence>,
	private val mapperCommon: Mapper<ProfilePersistence, Profile>
): ProfileDataSource {

	override fun refreshProfile(): Single<Profile> = Single.defer {
		authStorage
			.getAuthToken()
			.map { if (it.isEmpty()) throw TokenNotFounded else it  }
			.flatMap(profileService::loadProfile)
			.map(mapperRemote::transform)
			.blockingCompletable(profileStorage::saveProfile)
			.map(mapperCommon::transform)
	}

	override fun getProfile(): Maybe<Profile> = Maybe.defer {
		profileStorage
			.getProfile()
			.map(mapperCommon::transform)
	}

	override fun deleteProfile(): Completable = Completable.defer {
		profileStorage.deleteAll()
			.andThen(authStorage.setAuthToken(EMPTY))
	}
}
