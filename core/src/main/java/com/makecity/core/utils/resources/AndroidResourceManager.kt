package com.makecity.core.utils.resources

import android.content.Context
import com.makecity.core.utils.resources.ResourceManager
import javax.inject.Inject


class AndroidResourceManager @Inject constructor(
        private val context: Context
): ResourceManager {
    override fun getString(id: Int): String = context.getString(id)
}
