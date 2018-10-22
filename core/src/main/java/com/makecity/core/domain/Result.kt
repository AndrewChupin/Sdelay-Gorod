package com.makecity.core.domain


data class Result<Data>(
	val data: Data? = null,
	val error: Throwable? = null
) {

	val isSuccess: Boolean
		get() = data != null && error == null

	fun doIfSuccess(closure: (Data) -> Unit) {
		if (isSuccess) {
			data?.let(closure)
		}
	}
}