package com.makecity.core.di.module

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.squareup.moshi.Moshi
import com.makecity.core.BuildConfig
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


object NetworkSettings {
	const val NETWORK_READ_TIMEOUT = 25L
	const val NETWORK_WRITE_TIMEOUT = 25L
}


@Module
class NetworkModule {

	@Provides
	@Singleton
	fun provideMoshi(): Moshi = Moshi.Builder()
		.build()

	@Provides
	@Singleton
	fun provideOkHttp(): OkHttpClient {
		val builder = OkHttpClient.Builder()

		if (BuildConfig.DEBUG) {
			builder.addNetworkInterceptor(StethoInterceptor())
			val loggingInterceptor = HttpLoggingInterceptor()
			loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
			builder.addInterceptor(loggingInterceptor)
		}

		return builder.readTimeout(NetworkSettings.NETWORK_READ_TIMEOUT, TimeUnit.SECONDS)
			.writeTimeout(NetworkSettings.NETWORK_WRITE_TIMEOUT, TimeUnit.SECONDS)
			.build()
	}

	@Provides
	@Singleton
	fun provideRetorift(
		okHttpClient: OkHttpClient, moshi: Moshi
	): Retrofit = Retrofit.Builder()
		.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
		.addConverterFactory(MoshiConverterFactory.create(moshi))
		.client(okHttpClient)
		.baseUrl(BuildConfig.URL_API)
		.build()

}
