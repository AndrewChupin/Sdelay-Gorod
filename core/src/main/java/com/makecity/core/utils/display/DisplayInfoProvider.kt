package com.makecity.core.utils.display


/**
 * Provide display dimension type
 * </br>
 * @author Andrew Chupin
 */
interface DisplayInfoProvider {

	/**
	 * Calculate screen display type
	 * @return [mdpi, hdpi, xhdpi. xxhdpi, xxxhdpi] or only some
	 */
	fun provideDisplayType(): String

}
