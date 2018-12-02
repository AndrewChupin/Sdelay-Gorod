package com.makecity.client.data.common

import com.makecity.client.data.address.GeoCodeResponseRemote
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleApi {

	@GET("geocode/json?language=ru")
	fun getAddressBy(
		@Query("latlng") lat: String,
		@Query("key") key: String
	): Observable<GeoCodeResponseRemote>
}

