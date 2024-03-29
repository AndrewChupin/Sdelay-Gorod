package com.makecity.core.extenstion

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single


inline fun <reified T> Observable<T>.blockingCompletable(
	crossinline closure: (T) -> Completable
): Observable<T> = flatMap { closure(it).andThen(Observable.just(it)) }


inline fun <reified T> Single<T>.blockingCompletable(
	crossinline closure: (T) -> Completable
): Single<T> = flatMap { closure(it).andThen(Single.just(it)) }
