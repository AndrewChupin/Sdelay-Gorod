package com.makecity.core.presentation.view.map

import android.os.Parcelable
import com.makecity.core.data.Presentation
import kotlinx.android.parcel.Parcelize


@Presentation
@Parcelize
data class MapState(
	val position: Position
) : Parcelable


@Presentation
@Parcelize
data class Position(
	val latitude: Double,
	val longitude: Double,
	val zoom: Float
) : Parcelable