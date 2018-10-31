package com.makecity.client.data.company

import com.makecity.client.data.common.Api
import io.reactivex.Single
import javax.inject.Inject

interface CompanyService {

	fun loadCompanies(): Single<List<CompanyRemote>>

}


class CompanyServiceRetrofit @Inject constructor(
	private val api: Api
): CompanyService {

	override fun loadCompanies(): Single<List<CompanyRemote>> = api.loadCompanies()

}