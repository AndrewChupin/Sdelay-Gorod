package com.makecity.client.data.profile

import android.arch.persistence.room.ColumnInfo


data class ProfilePersistence(
	@ColumnInfo(name = "id") val id: Long,
	@ColumnInfo(name = "city_id") val cityId: Long,
	@ColumnInfo(name = "phone") val phone: String,
	@ColumnInfo(name = "image") val image: String,
	@ColumnInfo(name = "name") val name: String
)


data class Profile(
	val id: Long,
	val cityId: Long,
	val name: String,
	val image: String,
	val phone: String
)
