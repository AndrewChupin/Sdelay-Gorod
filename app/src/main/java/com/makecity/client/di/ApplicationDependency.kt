package com.makecity.client.di

import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import com.makecity.client.BuildConfig
import com.makecity.client.app.AppDelegate
import com.makecity.client.data.common.Api
import com.makecity.client.data.common.AppDatabase
import com.makecity.core.di.module.NavigationModule
import com.makecity.core.di.module.NetworkModule
import com.makecity.core.di.module.ResourceModule
import com.makecity.core.plugin.connection.AndroidConnectionProvider
import com.makecity.core.plugin.connection.ConnectionProvider
import com.makecity.core.utils.display.AndroidDisplayProvider
import com.makecity.core.utils.display.DisplayInfoProvider
import com.makecity.core.utils.image.CommonImageManager
import com.makecity.core.utils.image.ImageManager
import dagger.*
import retrofit2.Retrofit
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
    fun provideApi(retrofit: Retrofit): Api = retrofit.create(Api::class.java)

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
    fun provideConnectionProvider(provider: AndroidConnectionProvider): ConnectionProvider
}
