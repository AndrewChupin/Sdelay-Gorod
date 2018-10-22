package com.makecity.client.data.temp_problem

import com.makecity.client.data.common.AppDatabase
import io.reactivex.Completable
import io.reactivex.Maybe
import javax.inject.Inject


interface TempProblemStorage {

	fun saveTempProblem(tempProblemPersistence: TempProblemPersistence): Completable

	fun getTempProblem(): Maybe<TempProblemPersistence>
}


class TempProblemStorageRoom @Inject constructor(
	private val appDatabase: AppDatabase
) : TempProblemStorage {

	override fun saveTempProblem(tempProblemPersistence: TempProblemPersistence): Completable = Completable.fromCallable {
		appDatabase.getTempProblemDao().insert(tempProblemPersistence)
	}

	override fun getTempProblem(): Maybe<TempProblemPersistence> = Maybe.defer {
		val result = appDatabase.getTempProblemDao().findFirst() ?: return@defer Maybe.empty<TempProblemPersistence>()
		 Maybe.just(result)
	}
}
