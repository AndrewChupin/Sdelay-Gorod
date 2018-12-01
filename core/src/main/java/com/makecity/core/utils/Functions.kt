package com.makecity.core.utils


object Functions {
	val emptyFun: () -> Unit = {}
	val emptyErrorFun: (Throwable) -> Unit = {
		log(it)
	}
}