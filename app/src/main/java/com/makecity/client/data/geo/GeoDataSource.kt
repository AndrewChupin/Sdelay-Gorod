package com.makecity.client.data.geo

import com.makecity.core.extenstion.blockingCompletable
import io.reactivex.Completable
import io.reactivex.Maybe
import javax.inject.Inject


interface GeoDataSource {
	fun refreshCity(): Completable
	fun getDefaultGeoPoint(): Maybe<GeoPoint>
}


class GeoDataSourceDefault @Inject constructor(
	private val geoService: GeoService,
	private val geoPointStorage: GeoPointStorage,
	private val mapperRemoteToPersist: GeoPointRemoteToPersist,
	private val mapperPersistToCommon: GeoPointPersistToCommon
): GeoDataSource {

	override fun refreshCity(): Completable = geoService
		.getUserIp()
		.flatMap(geoService::getUserCity)
		.map(mapperRemoteToPersist::transform)
		.blockingCompletable(geoPointStorage::replaceGeoPoint)
		.ignoreElement()


	override fun getDefaultGeoPoint(): Maybe<GeoPoint> = geoPointStorage
		.getGeoStorage()
		.map(mapperPersistToCommon::transform)

}