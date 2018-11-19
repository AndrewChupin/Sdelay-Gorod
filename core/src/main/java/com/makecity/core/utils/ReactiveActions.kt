package com.makecity.core.utils

import android.util.Log
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


/**
 * Helper for all types of [Observer] - [Single] [Observable] [Maybe] [Flowable] [Completable]
 *
 * @defaults subscribeOn - Schedulers.io() | observeOn - AndroidSchedulers.mainThread()
 * @author Andrew Chupin
 */
interface ReactiveActions {

    /**
     * [CompositeDisposable] keeping all [Disposable] from [bindSubscribe]
     * You need set it when implement this interface
     */
    val disposables: CompositeDisposable

    /**
     * Automatically puts [Disposable] inside [disposables]
     */
    fun Disposable.bindDisposable() {
        disposables.add(this)
    }

    fun Disposable.tryAddDisposable() = disposables.add(this)

    /**
     * Helper for [Single]
     * @default [scheduler] - Schedulers.io()
     * @default [onSuccess] - (T) -> Unit
     * @default [onError] - (Throwable) -> Unit
     */
    fun <T> Single<T>.bindSubscribe(scheduler: Scheduler = Schedulers.io(),
                                         onSuccess: (T) -> Unit = {},
                                         onError: (Throwable) -> Unit = {
                                             Log.e("Error", "Reactive", it)
                                         })
            = subscribeOn(scheduler)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onSuccess, onError)
            .bindDisposable()

    /**
     * Helper for [Single]
     * @default [scheduler] - Schedulers.io()
     * @default [onSuccess] - (T) -> Unit
     * @default [onError] - (Throwable) -> Unit
     */
    fun <T> Maybe<T>.bindSubscribe(scheduler: Scheduler = Schedulers.io(),
                                        onSuccess: (T) -> Unit = {},
                                        onError: (Throwable) -> Unit  = {
                                            Log.e("Error", "Reactive", it)
                                        },
                                        onComplete: () -> Unit = {})
            = subscribeOn(scheduler)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onSuccess, onError, onComplete)
            .bindDisposable()

    /**
     * Helper for [Observable]
     * @default [scheduler] - Schedulers.io()
     * @default [onNext] - (T) -> Unit
     * @default [onError] - (Throwable) -> Unit
     * @default [onComplete] - () -> Unit
     */
    fun <T> Observable<T>.bindSubscribe(scheduler: Scheduler = Schedulers.io(),
                                             onNext: (T) -> Unit = {},
                                             onError: (Throwable) -> Unit  = {
                                                 Log.e("Error", "Reactive", it)
                                             },
                                             onComplete: () -> Unit = {})
            = subscribeOn(scheduler)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onNext, onError, onComplete)
            .bindDisposable()

    /**
     * Helper for [Flowable]
     * @default [scheduler] - Schedulers.io()
     * @default [onNext] - (T) -> Unit
     * @default [onError] - (Throwable) -> Unit
     * @default [onComplete] - () -> Unit
     */
    fun <T> Flowable<T>.bindSubscribe(scheduler: Scheduler = Schedulers.io(),
                                           onNext: (T) -> Unit = {},
                                           onError: (Throwable) -> Unit = {
                                               Log.e("Error", "Reactive", it)
                                           },
                                           onComplete: () -> Unit = {})
            = subscribeOn(scheduler)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onNext, onError, onComplete)
            .bindDisposable()

    /**
     * Helper for [Completable]
     * @default [scheduler] - Schedulers.io()
     * @default [onSuccess] - () -> Unit
     * @default [onError] - (Throwable) -> Unit
     */
    fun Completable.bindSubscribe(scheduler: Scheduler = Schedulers.io(),
                                  onSuccess: () -> Unit = {},
                                  onError: (Throwable) -> Unit  = {
                                      Log.e("Error", "Reactive", it)
                                  })
            = subscribeOn(scheduler)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onSuccess, onError)
            .bindDisposable()

    /**
     * Clear all keeping disposables inside [disposables]
     * You must clear it to avoid memory leaks
     */
    fun clearDisposables() = disposables.clear()
}
