package com.makecity.client.data.geo

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.makecity.core.data.BaseDao

@Dao
abstract class GeoPointDao: BaseDao<GeoPointPersistence> {

	@Query("DELETE FROM geo_point")
	abstract fun deleteAll()

	@Query("SELECT * FROM geo_point LIMIT 1")
	abstract fun findFirst(): GeoPointPersistence?

}