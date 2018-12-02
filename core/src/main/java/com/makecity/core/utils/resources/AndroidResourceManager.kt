package com.makecity.core.utils.resources

import android.content.Context
import javax.inject.Inject

class AndroidResourceManager @Inject constructor(
	private val context: Context
) : ResourceManager {
	override fun getString(id: Int): String = context.getString(id)
}
