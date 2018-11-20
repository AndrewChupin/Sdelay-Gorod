package com.makecity.client.data.geo

import com.makecity.client.data.common.Api
import io.reactivex.Single
import javax.inject.Inject


interface GeoService {

	fun getUserIp(): Single<String>

	fun getUserCity(ip: String): Single<GeoPointRemote>

	fun loadActiveCities(): Single<List<GeoPointRemote>>
}


class GeoServiceRetrofit @Inject constructor (
	private val api: Api
): GeoService {

	override fun getUserIp(): Single<String> = api.getIp()

	override fun getUserCity(ip: String): Single<GeoPointRemote>  = api.getCity(ip)

	override fun loadActiveCities(): Single<List<GeoPointRemote>> = api.getCities()
}