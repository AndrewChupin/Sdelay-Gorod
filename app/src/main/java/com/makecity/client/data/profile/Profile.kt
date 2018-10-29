package com.makecity.client.data.profile

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.makecity.core.data.Dto
import com.squareup.moshi.Json


@Dto
data class ProfileRemote(
	@Json(name = "born") val born: String?,
	@Json(name = "sex") val sex: String?,
	@Json(name = "first_name") val firstName: String?,
	@Json(name = "last_name") val lastName: String?,
	@Json(name = "city_id") val cityId: Long?,
	@Json(name = "street") val street: String?,
	@Json(name = "house") val house: String?,
	@Json(name = "default_org_id") val defaultCompanyId: Long?,
	@Json(name = "email") val email: String?,
	@Json(name = "user") val user: UserRemote
)


@Dto
data class UserRemote(
	@Json(name = "foto") val photo: String?,
	@Json(name = "phone") val phone: String?,
	@Json(name = "role") val role: String?,
	@Json(name = "org") val org: Long?
)


@Entity(tableName = "profile")
data class ProfilePersistence(
	@PrimaryKey(autoGenerate = true) val id: Long = 0,
	@ColumnInfo(name = "city_id") val cityId: Long,
	@ColumnInfo(name = "phone") val phone: String,
	@ColumnInfo(name = "photo") val photo: String,
	@ColumnInfo(name = "first_name") val firstName: String,
	@ColumnInfo(name = "last_name") val lastName: String,
	@ColumnInfo(name = "street") val street: String,
	@ColumnInfo(name = "house") val house: String,
	@ColumnInfo(name = "default_org_id") val defaultCompanyId: Long
)


data class Profile(
	val id: Long = 0,
	val cityId: Long,
	val phone: String,
	val photo: String,
	val firstName: String,
	val lastName: String,
	val street: String,
	val house: String,
	val defaultCompanyId: Long
)

