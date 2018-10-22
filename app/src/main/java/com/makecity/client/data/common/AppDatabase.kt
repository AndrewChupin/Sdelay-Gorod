package com.makecity.client.data.common

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.makecity.client.data.geo.GeoPointDao
import com.makecity.client.data.geo.GeoPointPersistence

@Database(entities = [
	GeoPointPersistence::class
], version = 1, exportSchema = true)
abstract class AppDatabase: RoomDatabase() {
	abstract fun getGeoPointDao(): GeoPointDao
}