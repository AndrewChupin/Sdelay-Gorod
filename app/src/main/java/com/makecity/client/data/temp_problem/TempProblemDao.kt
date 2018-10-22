package com.makecity.client.data.temp_problem

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.makecity.core.data.BaseDao

@Dao
abstract class TempProblemDao: BaseDao<TempProblemPersistence> {

	@Query("DELETE FROM temp_problem")
	abstract fun deleteAll()

	@Query("SELECT * FROM temp_problem LIMIT 1")
	abstract fun findFirst(): TempProblemPersistence?
}