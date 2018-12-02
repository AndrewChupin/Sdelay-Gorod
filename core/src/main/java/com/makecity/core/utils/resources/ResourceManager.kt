package com.makecity.core.utils.resources

/**
 * Interface wrapper for access resources
 * </br>
 * If need other types you can add here
 * @author Andrew Chupin
 */
interface ResourceManager {
	/**
	 * Access to [String] resources
	 */
	fun getString(id: Int): String
}
