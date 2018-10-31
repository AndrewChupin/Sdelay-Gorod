package com.makecity.client.data.company

import com.makecity.core.domain.Mapper
import io.reactivex.Single
import javax.inject.Inject


interface CompanyDataSource {
	fun getCompanies(): Single<List<Company>>
}


class CompanyDataSourceDefault @Inject constructor (
	private val companyService: CompanyService,
	private val mapper: Mapper<CompanyRemote, Company>
) : CompanyDataSource {

	override fun getCompanies(): Single<List<Company>> = Single.defer {
		companyService
			.loadCompanies()
			.map(mapper::transformAll)
	}
}