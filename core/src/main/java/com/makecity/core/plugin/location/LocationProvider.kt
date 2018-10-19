package com.makecity.core.plugin.location

import com.makecity.core.data.entity.Location
import io.reactivex.Flowable
import io.reactivex.Single


/**
 * Interface wrapper for observe locatiion
 * @author Andrew Chupin
 */
interface LocationProvider {

	/**
	 * Get current location state
	 */
	fun getLocation(): Single<Location>

	/**
	 * Observe location state changed
	 */
	fun observeLocationUpdates(): Flowable<Location>

}
