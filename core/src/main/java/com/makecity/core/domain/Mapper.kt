package com.makecity.core.domain


/**
 * Abstraction for transformation between two classes
 * </br>
 * [From] and [To] without `in` and `out`(wildcards) operators because that kapt is unsupported this
 * @author Andrew Chupin
 */
interface Mapper<From, To> {

	/**
	 * Apply transform
	 * @param [entity] - object for transform
	 * @return [To] - transformed class
	 */
	fun transform(entity: From): To

	fun transformAll(from: List<From>): List<To> = from.map(::transform)
}
