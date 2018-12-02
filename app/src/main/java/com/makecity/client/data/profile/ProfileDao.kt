package com.makecity.client.data.profile

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.makecity.core.data.BaseDao


@Dao
abstract class ProfileDao : BaseDao<ProfilePersistence> {

	@Query("SELECT * FROM profile LIMIT 1")
	abstract fun findFirst(): ProfilePersistence?

	@Query("DELETE FROM profile")
	abstract fun deleteAll()
}