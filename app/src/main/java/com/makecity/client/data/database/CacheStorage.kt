package com.makecity.client.data.database

import android.util.LruCache
import com.makecity.core.extenstion.moreThanNow
import io.reactivex.Completable
import io.reactivex.Maybe
import java.util.concurrent.TimeUnit


interface Cache<Data> {
	val data: Data
	val strategy: CacheStrategy
}


interface CacheStorage {
	fun <Data> add(key: String, data: Cache<Data>): Completable

	fun <Data> pop(key: String): Maybe<Cache<Data>>

	fun <Data> get(key: String): Maybe<Cache<Data>>

	fun dropAll(): Completable
}

interface CacheTime {
	val time: Long
	val unit: TimeUnit
}

sealed class CacheStrategy {
	object Forever: CacheStrategy()

	object Soft: CacheStrategy()

	data class Time(
		override val time: Long,
		override val unit: TimeUnit
	): CacheStrategy(), CacheTime

	data class SoftTime(
		override val time: Long,
		override val unit: TimeUnit
	): CacheStrategy(), CacheTime
}


class CacheStorageInMemory(
	private val defaultLruSize: Int
): CacheStorage {

	private var cacheMap: MutableMap<String, Cache<*>> = mutableMapOf()
	private var lruCache: LruCache<String, Cache<*>> = LruCache(defaultLruSize)

	override fun <Data> add(key: String, cache: Cache<Data>): Completable = Completable.fromCallable {
		checkTimer()

		cacheMap.remove(key)
		lruCache.remove(key)

		when (cache.strategy) {
			is CacheStrategy.Soft -> lruCache.put(key, cache)
			is CacheStrategy.SoftTime -> lruCache.put(key, cache)
			else -> cacheMap.put(key, cache)
		}
	}

	override fun <Data> pop(key: String): Maybe<Cache<Data>> = Maybe.defer {
		checkTimer()
		/*if (cacheMap.containsKey(key)) {
			return@defer Maybe.just(cacheMap.remove(key)) as Maybe<Cache<Data>>
		}

		if (lruCache[key] != null) {
			return@defer Maybe.just(lruCache.remove(key)) as Maybe<Cache<Data>>
		}*/

		Maybe.empty<Cache<Data>>()
	}

	override fun <Data> get(key: String): Maybe<Cache<Data>> = Maybe.defer {
		checkTimer()
		/*if (cacheMap.containsKey(key)) {
			return@defer Maybe.just(cacheMap[key])
		}

		if (lruCache[key] != null) {
			return@defer Maybe.just(lruCache[key])
		}*/

		Maybe.empty<Cache<Data>>()
	}

	override fun dropAll(): Completable = Completable.fromCallable {
		checkTimer()
		lruCache.snapshot().forEach {
			lruCache.remove(it.key)
		}
	}

	private fun checkTimer() {
		cacheMap = cacheMap.filter {
			val strategy = it.value.strategy
			strategy is CacheTime && !strategy.unit.moreThanNow(strategy.time)
		}.toMutableMap()

		lruCache.snapshot().forEach {
			val strategy = it.value.strategy
			if (strategy is CacheTime && strategy.unit.moreThanNow(strategy.time)) {
				lruCache.remove(it.key)
			}
		}
	}
}