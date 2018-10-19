package com.makecity.core.data.entity

import android.os.Parcelable
import com.makecity.core.data.Common
import kotlinx.android.parcel.Parcelize


@Common
@Parcelize
data class Location(
	val latitude: Double,
	val longitude: Double
): Parcelable
