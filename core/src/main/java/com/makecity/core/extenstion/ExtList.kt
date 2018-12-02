package com.makecity.core.extenstion

import com.makecity.core.domain.Mapper


inline fun <reified From, reified To> List<From>.transform(mapper: Mapper<From, To>): List<To> = this.map(mapper::transform)


inline fun <reified Data> List<Data>?.concat(data: List<Data>): List<Data> {
	val newData: MutableList<Data> = mutableListOf()
	this?.let {
		newData.addAll(it)
	}
	newData.addAll(data)
	return newData
}
