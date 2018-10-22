package com.makecity.client.data.temp_problem

import com.makecity.core.domain.Mapper
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

interface TempProblemDataSource {

	fun saveTempProblem(tempProblem: TempProblem) : Completable
	fun getTempProblem() : Single<TempProblem>
}


class TempProblemDataSourceDefault @Inject constructor(
	private val storage: TempProblemStorage,
	private val mapperCommonToPersistence: Mapper<TempProblem, TempProblemPersistence>,
	private  val mapperPersistenceToCommon: Mapper<TempProblemPersistence, TempProblem>
) : TempProblemDataSource {

	override fun saveTempProblem(tempProblem: TempProblem): Completable = storage
		.saveTempProblem(mapperCommonToPersistence.transform(tempProblem))

	override fun getTempProblem(): Single<TempProblem> = storage
		.getTempProblem()
		.map(mapperPersistenceToCommon::transform)
		.defaultIfEmpty(TempProblem())
		.toSingle()
}