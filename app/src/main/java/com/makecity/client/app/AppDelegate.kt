package com.makecity.client.app

import android.app.Application
import com.facebook.stetho.Stetho
import com.makecity.client.BuildConfig
import com.makecity.client.di.AppComponent
import com.makecity.client.di.common.DefaultInjectorPlugin


class AppDelegate: Application() {

	private lateinit var appComponent: AppComponent

	override fun onCreate() {
		super.onCreate()
		AppInjector.initInjection(DefaultInjectorPlugin)

		if (BuildConfig.DEBUG) {
			Stetho.initializeWithDefaults(this)
		}
		appComponent = AppInjector.injectApp(this)
	}
}