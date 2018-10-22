package com.makecity.client.app

import android.content.Context
import com.facebook.stetho.Stetho
import com.makecity.client.BuildConfig
import com.makecity.client.di.AppComponent
import com.makecity.client.di.common.DefaultInjectorPlugin
import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication


class AppDelegate: MultiDexApplication() {

	private lateinit var appComponent: AppComponent

	override fun attachBaseContext(base: Context) {
		super.attachBaseContext(base)
		MultiDex.install(this)
	}

	override fun onCreate() {
		super.onCreate()
		AppInjector.initInjection(DefaultInjectorPlugin)

		if (BuildConfig.DEBUG) {
			Stetho.initializeWithDefaults(this)
		}
		appComponent = AppInjector.injectApp(this)
	}
}