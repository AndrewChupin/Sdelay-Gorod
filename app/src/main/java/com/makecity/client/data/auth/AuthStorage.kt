package com.makecity.client.data.auth

import android.content.SharedPreferences
import com.makecity.core.utils.Symbols.EMPTY
import com.makecity.core.utils.WritePersistenceException
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject


interface AuthStorage {
	fun getToken(): Single<String>
	fun setToken(token: String): Completable
}


class AuthStoragePreferences @Inject constructor(
	private val preferences: SharedPreferences
) : AuthStorage {

	companion object {
		private const val KEY_AUTH_PREFERENCES_TOKEN = "KEY_AUTH_PREFERENCES_TOKEN"
	}

	override fun getToken(): Single<String> = Single.fromCallable {
		preferences.getString(KEY_AUTH_PREFERENCES_TOKEN, EMPTY)
	}

	override fun setToken(token: String): Completable = Completable.fromCallable {
		val isSuccess = preferences.edit()
			.putString(KEY_AUTH_PREFERENCES_TOKEN, token)
			.commit()

		if (!isSuccess) {
			Completable.error(WritePersistenceException)
		}
	}
}
