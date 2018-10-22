package com.makecity.client.data.geo

import com.makecity.client.data.common.Api
import io.reactivex.Single


interface GeoService {

	fun getUserIp(): Single<String>

	fun getUserCity(ip: String): Single<GeoPointRemote>

}


class GeoServiceRetrofit(
	private val api: Api
): GeoService {

	override fun getUserIp(): Single<String> = api.getIp()

	override fun getUserCity(ip: String): Single<GeoPointRemote>  = api.getCity(ip)

}