package com.makecity.core.presentation.view.map

import com.makecity.core.data.entity.Location

// Camera States
sealed class CameraState
object InitMoving : CameraState()
object StartMoving : CameraState()
data class StopMoving(
	val centerLocation: Location,
	val locationSw: Location,
	val locationNe: Location,
	val zoom: Float,
	val radius: Int
): CameraState()
