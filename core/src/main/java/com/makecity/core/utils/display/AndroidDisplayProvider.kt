package com.makecity.core.utils.display

import android.content.Context
import com.makecity.core.extenstion.displayType
import javax.inject.Inject


class AndroidDisplayProvider @Inject constructor(
	private val context: Context
): DisplayInfoProvider {

	override fun provideDisplayType(): String = context.displayType

}
