package com.makecity.client.data.temp_problem

import com.makecity.client.data.auth.AuthDataSource
import com.makecity.client.data.geo.GeoDataSource
import com.makecity.client.data.geo.GeoPoint
import com.makecity.core.domain.Mapper
import com.makecity.core.extenstion.blockingCompletable
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import javax.inject.Inject

interface TempProblemDataSource {

	fun saveTempProblem(tempProblem: TempProblem) : Completable
	fun getTempProblem() : Single<TempProblem>
	fun isProblemExist() : Single<Boolean>
	fun createTask(tempProblem: TempProblem) : Single<Boolean>
	fun deleteAll(): Completable
}


class TempProblemDataSourceDefault @Inject constructor(
	private val storage: TempProblemStorage,
	private val tempTaskService: TempTaskService,
	private val geoDataSource: GeoDataSource,
	private val authDataSource: AuthDataSource,
	private val mapperCommonToPersistence: Mapper<TempProblem, TempProblemPersistence>,
	private  val mapperPersistenceToCommon: Mapper<TempProblemPersistence, TempProblem>
) : TempProblemDataSource {


	override fun saveTempProblem(tempProblem: TempProblem): Completable = Completable.defer {
		storage.saveTempProblem(mapperCommonToPersistence.transform(tempProblem))
	}

	override fun getTempProblem(): Single<TempProblem> = Single.defer {
		storage
			.getTempProblem()
			.map(mapperPersistenceToCommon::transform)
			.defaultIfEmpty(TempProblem())
			.toSingle()
	}

	override fun createTask(tempProblem: TempProblem): Single<Boolean> = Single.defer {
		Single.zip<GeoPoint, String, CreateTaskRequest>(
			geoDataSource.getDefaultGeoPoint().toSingle(),
			authDataSource.checkToken(), BiFunction { geoPoint, token ->
				CreateTaskRequest(tempProblem, geoPoint, token)
			})
			.flatMap(tempTaskService::createTask)
			.blockingCompletable { storage.clear() }
	}

	override fun isProblemExist(): Single<Boolean> = Single.defer {
		storage.isTempProblemExist()
	}

	override fun deleteAll(): Completable = Completable.defer {
		storage.clear()
	}
}