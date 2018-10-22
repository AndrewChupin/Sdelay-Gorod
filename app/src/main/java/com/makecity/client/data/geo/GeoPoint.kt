package com.makecity.client.data.geo

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.makecity.core.data.Common
import com.makecity.core.data.Dto
import com.makecity.core.data.Persistence
import com.squareup.moshi.Json


@Dto
data class GeoPointRemote(
	@Json(name = "city_id") val cityId: Long,
	@Json(name = "country_id") val countryId: Long,
	@Json(name = "important") val important: Boolean?,
	@Json(name = "region_id") val regionId: Long,
	@Json(name = "title_ru") val title: String?,
	@Json(name = "area_ru") val area: String?,
	@Json(name = "region_ru") val region: String?,
	@Json(name = "lat") val latitude: Double,
	@Json(name = "lon") val longitude: Double
)


@Persistence
@Entity(tableName = "geo_point")
data class GeoPointPersistence(
	@PrimaryKey val cityId: Long,
	@ColumnInfo(name = "country_id") val countryId: Long,
	@ColumnInfo(name = "important") val important: Boolean,
	@ColumnInfo(name = "region_id") val regionId: Long,
	@ColumnInfo(name = "title") val title: String,
	@ColumnInfo(name = "area") val area: String,
	@ColumnInfo(name = "region") val region: String,
	@ColumnInfo(name = "latitude") val latitude: Double,
	@ColumnInfo(name = "longitude") val longitude: Double
)


@Common
data class GeoPoint(
	val cityId: Long,
	val countryId: Long,
	val important: Boolean,
	val regionId: Long,
	val title: String,
	val area: String,
	val region: String,
	val latitude: Double,
	val longitude: Double
)
