package com.makecity.client.data.profile

import io.reactivex.Completable
import io.reactivex.Single


interface ProfileStorage {
	fun saveProfile(profilePersistence: ProfilePersistence): Completable
	fun getProfile(): Single<ProfilePersistence>
}

