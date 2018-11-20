package com.makecity.client.data.geo

import com.makecity.core.domain.Mapper
import com.makecity.core.extenstion.blockingCompletable
import com.makecity.core.utils.Symbols
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import javax.inject.Inject


interface GeoDataSource {
	fun refreshCity(): Completable
	fun getDefaultGeoPoint(): Maybe<GeoPoint>
	fun getActiveCities(): Single<List<GeoPoint>>
	fun setDefaultCity(getPoint: GeoPoint): Completable
}


class GeoDataSourceDefault @Inject constructor(
	private val geoService: GeoService,
	private val geoPointStorage: GeoPointStorage,
	private val mapperRemoteToPersist: Mapper<GeoPointRemote, GeoPointPersistence>,
	private val mapperPersistToCommon: Mapper<GeoPointPersistence, GeoPoint>
): GeoDataSource {

	override fun refreshCity(): Completable = geoService
		.getUserIp()
		.flatMap(geoService::getUserCity)
		.map(mapperRemoteToPersist::transform)
		.flatMapCompletable(geoPointStorage::replaceGeoPoint)


	override fun getDefaultGeoPoint(): Maybe<GeoPoint> = geoPointStorage
		.getGeoStorage()
		.map(mapperPersistToCommon::transform)


	override fun getActiveCities(): Single<List<GeoPoint>> = geoService
		.loadActiveCities()
		.map(mapperRemoteToPersist::transformAll)
		.map(mapperPersistToCommon::transformAll)


	override fun setDefaultCity(getPoint: GeoPoint): Completable = geoPointStorage
		.replaceGeoPoint(getPoint.run { // TODO LATE CREATE MAPPER
			GeoPointPersistence(
				cityId = cityId,
				countryId = countryId,
				regionId = regionId,
				important = important,
				region = region,
				area = area,
				title = title,
				latitude = latitude,
				longitude = longitude
			)
		})
}