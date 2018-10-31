package com.makecity.client.data.profile

import com.makecity.client.data.common.AppDatabase
import io.reactivex.Completable
import io.reactivex.Maybe
import javax.inject.Inject


interface ProfileStorage {
	fun saveProfile(profilePersistence: ProfilePersistence): Completable
	fun getProfile(): Maybe<ProfilePersistence>
	fun deleteAll(): Completable
}


class ProfileStorageRoom @Inject constructor(
	private val appDatabase: AppDatabase
): ProfileStorage {

	override fun saveProfile(profilePersistence: ProfilePersistence): Completable = Completable.fromCallable {
		appDatabase.getProfileDao().insert(profilePersistence)
	}

	override fun getProfile(): Maybe<ProfilePersistence> = Maybe.defer {
		val profile = appDatabase.getProfileDao().findFirst()
			?: return@defer Maybe.empty<ProfilePersistence>()
		Maybe.just(profile)
	}

	override fun deleteAll(): Completable = Completable.fromCallable {
		appDatabase
			.getProfileDao()
			.deleteAll()
	}
}