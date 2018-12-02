package com.makecity.core.data.remote

import io.reactivex.Single
import io.reactivex.SingleSource
import io.reactivex.SingleTransformer
import retrofit2.HttpException


class SingleHttpExceptionHandler<T> : SingleTransformer<T, T> {

	override fun apply(upstream: Single<T>): SingleSource<T> = upstream
		.onErrorResumeNext {
			when (it) {
				is HttpException -> it.specifyError()
				else -> Single.error(it)
			}
		}

	private fun <T> HttpException.specifyError(): Single<T> = when (code()) {
		401 -> Single.error(this)
		else -> Single.error(this)
	}

}
