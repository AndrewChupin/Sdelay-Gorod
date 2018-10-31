package com.makecity.client.data.company

import com.makecity.core.domain.Mapper
import com.makecity.core.utils.Symbols.EMPTY
import javax.inject.Inject


class CompanyMapperDtoToCommon @Inject constructor(): Mapper<CompanyRemote, Company> {

	override fun transform(entity: CompanyRemote): Company = entity.run {
		Company(
			id = id,
			cityId = cityId,
			name = name ?: EMPTY,
			area = area ?: EMPTY,
			phone = phone ?: EMPTY,
			logo = logo ?: EMPTY
		)
	}
}