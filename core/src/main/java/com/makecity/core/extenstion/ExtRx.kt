package com.makecity.core.extenstion

import io.reactivex.Completable
import io.reactivex.Observable


inline fun <reified T> Observable<T>.blockingCompletable(
	crossinline closure: (T) -> Completable
): Observable<T> = flatMap { closure(it).andThen(Observable.just(it)) }
