package com.makecity.client.data.temp_problem

import com.makecity.core.domain.Mapper
import javax.inject.Inject


class TempProblemMapperCommonToPersistence @Inject constructor() : Mapper<TempProblem, TempProblemPersistence> {

	override fun transform(entity: TempProblem): TempProblemPersistence = entity.run {
		TempProblemPersistence(
			id = id,
			categoryId = categoryId,
			optionId = optionId,
			categoryName = categoryName,
			optionName = optionName,
			imageFirst = imageFirst,
			imageSecond = imageSecond,
			description = description,
			latitude = latitude,
			longitude = longitude,
			address = address
		)
	}
}


class TempProblemMapperPersistenceToCommon @Inject constructor() : Mapper<TempProblemPersistence, TempProblem> {

	override fun transform(entity: TempProblemPersistence): TempProblem = entity.run {
		TempProblem(
			id = id,
			categoryId = categoryId,
			optionId = optionId,
			imageFirst = imageFirst,
			categoryName = categoryName,
			optionName = optionName,
			imageSecond = imageSecond,
			description = description,
			latitude = latitude,
			longitude = longitude,
			address = address
		)
	}
}