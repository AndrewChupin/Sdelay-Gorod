package com.makecity.core.plugin.location

import com.makecity.core.data.entity.Location


sealed class LocationState {
	object Unknown : LocationState()
	data class Founded(
		val location: Location
	) : LocationState()

	data class Failure(
		val error: Throwable
	) : LocationState()
}
