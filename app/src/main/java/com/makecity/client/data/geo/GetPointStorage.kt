package com.makecity.client.data.geo

import com.makecity.client.data.common.AppDatabase
import io.reactivex.Completable
import io.reactivex.Maybe
import javax.inject.Inject


interface GeoPointStorage {
	fun replaceGeoPoint(geoPoint: GeoPointPersistence): Completable
	fun getGeoStorage(): Maybe<GeoPointPersistence>
}


class GeoPointStoragePreference @Inject constructor(
	private val appDatabase: AppDatabase
) : GeoPointStorage {

	override fun replaceGeoPoint(geoPoint: GeoPointPersistence): Completable = Completable.fromCallable {
		appDatabase.getGeoPointDao().insert(geoPoint)
	}

	override fun getGeoStorage(): Maybe<GeoPointPersistence> = Maybe.defer {
		val geoPoint = appDatabase.getGeoPointDao().findFirst()
			?: return@defer Maybe.empty<GeoPointPersistence>()

		Maybe.just(geoPoint)
	}
}