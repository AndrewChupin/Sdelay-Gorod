package com.makecity.client.di

import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.makecity.client.BuildConfig
import com.makecity.client.app.AppDelegate
import com.makecity.client.data.address.AddressAdapter
import com.makecity.client.data.common.Api
import com.makecity.client.data.common.AppDatabase
import com.makecity.client.data.common.GoogleApi
import com.makecity.client.di.common.BaseApiQualifier
import com.makecity.client.di.common.GeoApiQualifier
import com.makecity.client.utils.saver.FileSaverBitmap
import com.makecity.core.di.module.NavigationModule
import com.makecity.core.di.module.PagingModule
import com.makecity.core.di.module.ResourceModule
import com.makecity.core.plugin.connection.AndroidConnectionProvider
import com.makecity.core.plugin.connection.ConnectionProvider
import com.makecity.core.utils.display.AndroidDisplayProvider
import com.makecity.core.utils.display.DisplayInfoProvider
import com.makecity.core.utils.image.CommonImageManager
import com.makecity.core.utils.image.ImageManager
import com.makecity.core.utils.saver.FileSaver
import com.squareup.moshi.Moshi
import dagger.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Singleton
@Component(modules = [
    NetworkModule::class,
    DataModule::class,
    NavigationModule::class,
    ResourceModule::class,
    ProviderModule::class
])
interface AppComponent{

    // Injects
    fun inject(appDelegate: AppDelegate)

    // Subcomponents
    fun mainComponent(): MainComponent.Builder

    // Builder
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun withApplication(application: Application): Builder
        fun build(): AppComponent
    }
}


@Module
open class DataModule {

    @Provides
    @Singleton
    open fun provideDatabase(application: Application): AppDatabase =
            Room.databaseBuilder(application, AppDatabase::class.java, BuildConfig.APP_DATABASE_NAME)
                    .build()

    @Provides
    @Singleton
    fun providePreferences(application: Application): SharedPreferences =
            application.getSharedPreferences(BuildConfig.APP_REQUEST_PREFERECES_NAME, Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideApi(@BaseApiQualifier retrofit: Retrofit): Api = retrofit.create(Api::class.java)

    @Provides
    @Singleton
    fun provideGeoApi(@GeoApiQualifier retrofit: Retrofit): GoogleApi = retrofit.create(GoogleApi::class.java)

    @Provides
    @Singleton
    fun provideContext(application: Application): Context = application.baseContext


	@Provides
	@Singleton
	fun provideConnectivityManager(context: Context): ConnectivityManager
		= context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

}


@Module
interface ProviderModule {

    @Singleton
    @Binds
    fun provideAndroidDisplayProvider(provider: AndroidDisplayProvider): DisplayInfoProvider

    @Singleton
    @Binds
    fun provideTempGlideImageManager(manager: CommonImageManager): ImageManager

    @Singleton
    @Binds
    fun provideFileSaverBitmap(manager: FileSaverBitmap): FileSaver

    @Singleton
    @Binds
    fun provideConnectionProvider(provider: AndroidConnectionProvider): ConnectionProvider
}


object NetworkSettings {
    const val NETWORK_READ_TIMEOUT = 25L
    const val NETWORK_WRITE_TIMEOUT = 25L
}


@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(AddressAdapter())
        .build()

    @Provides
    @Singleton
    fun provideOkHttp(): OkHttpClient {
        val builder = OkHttpClient.Builder()

        if (com.makecity.core.BuildConfig.DEBUG) {
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
    @BaseApiQualifier
    fun provideBaseRetorift(
        okHttpClient: OkHttpClient, moshi: Moshi
    ): Retrofit = Retrofit.Builder()
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(okHttpClient)
        .baseUrl(BuildConfig.URL_API)
        .build()

    @Provides
    @Singleton
    @GeoApiQualifier
    fun provideGeoRetorift(
        okHttpClient: OkHttpClient, moshi: Moshi
    ): Retrofit = Retrofit.Builder()
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(okHttpClient)
        .baseUrl(BuildConfig.URL_GOOGLE_API)
        .build()

}
