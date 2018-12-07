package com.makecity.client.data.auth

import android.content.SharedPreferences
import com.makecity.core.utils.Symbols.EMPTY
import com.makecity.core.utils.WritePersistenceException
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject


interface AuthStorage {
	fun getAuthToken(): Single<String>
	fun setAuthToken(token: String): Completable

	fun getRegistrationToken(): Single<String>
	fun setRegistrationToken(token: String): Completable

	fun getPhone(): Single<String>
	fun setPhone(phone: String): Completable

	fun getSmsTimestamp(): Single<Long>
	fun setSmsTimestamp(timestamp: Long): Completable
}


class AuthStoragePreferences @Inject constructor(
	private val preferences: SharedPreferences
) : AuthStorage {

	companion object {
		private const val KEY_AUTH_REG_TOKEN = "KEY_AUTH_REG_TOKEN"
		private const val KEY_AUTH_TOKEN = "KEY_AUTH_TOKEN"
		private const val KEY_AUTH_PHONE = "KEY_AUTH_PHONE"
		private const val KEY_SMS_TIMESTAMP = "KEY_SMS_TIMESTAMP"
	}


	// Auth token
	override fun getAuthToken(): Single<String> = Single.fromCallable {
		preferences.getString(KEY_AUTH_TOKEN, EMPTY)
	}

	override fun setAuthToken(token: String): Completable = Completable.defer {
		val isSuccess = preferences.edit()
			.putString(KEY_AUTH_TOKEN, token)
			.commit()

		if (isSuccess) Completable.complete() else Completable.error(WritePersistenceException)
	}


	// Registration token
	override fun getRegistrationToken(): Single<String> = Single.fromCallable {
		preferences.getString(KEY_AUTH_REG_TOKEN, EMPTY)
	}

	override fun setRegistrationToken(token: String): Completable = Completable.defer {
		val isSuccess = preferences.edit()
			.putString(KEY_AUTH_REG_TOKEN, token)
			.commit()

		if (isSuccess) Completable.complete() else Completable.error(WritePersistenceException)
	}


	// Phone
	override fun getPhone(): Single<String> = Single.fromCallable {
		preferences.getString(KEY_AUTH_PHONE, EMPTY)
	}

	override fun setPhone(phone: String): Completable = Completable.defer {
		val isSuccess = preferences.edit()
			.putString(KEY_AUTH_PHONE, phone)
			.commit()

		if (isSuccess) Completable.complete() else Completable.error(WritePersistenceException)
	}


	// Phone
	override fun getSmsTimestamp(): Single<Long> = Single.fromCallable {
		preferences.getLong(KEY_SMS_TIMESTAMP, 0)
	}

	override fun setSmsTimestamp(timestamp: Long): Completable = Completable.defer {
		val isSuccess = preferences.edit()
			.putLong(KEY_SMS_TIMESTAMP, timestamp)
			.commit()

		if (isSuccess) Completable.complete() else Completable.error(WritePersistenceException)
	}
}
