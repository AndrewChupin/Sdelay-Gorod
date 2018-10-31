package com.makecity.client.data.common

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.makecity.client.data.geo.GeoPointDao
import com.makecity.client.data.geo.GeoPointPersistence
import com.makecity.client.data.profile.ProfileDao
import com.makecity.client.data.profile.ProfilePersistence
import com.makecity.client.data.temp_problem.TempProblemDao
import com.makecity.client.data.temp_problem.TempProblemPersistence

@Database(entities = [
	GeoPointPersistence::class,
	TempProblemPersistence::class,
	ProfilePersistence::class
], version = 1, exportSchema = true)
abstract class AppDatabase: RoomDatabase() {
	abstract fun getGeoPointDao(): GeoPointDao
	abstract fun getTempProblemDao(): TempProblemDao
	abstract fun getProfileDao(): ProfileDao
}