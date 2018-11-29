package com.makecity.client.presentation.main

import android.app.Activity
import android.app.Fragment
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.view.View
import com.makecity.client.R
import com.makecity.client.app.AppInjector
import com.makecity.client.di.MainComponent
import com.makecity.core.presentation.view.ReducibleViewActivity
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import javax.inject.Inject


typealias MainView = ReducibleViewActivity<MainReducer, MainAction>

class MainActivity : MainView() {

	@Inject
	lateinit var navigatorHolder: NavigatorHolder
	@Inject
	lateinit var navigator: Navigator

	override val layoutId: Int? = R.layout.activity_main

	private lateinit var mainComponent: MainComponent

	override fun onInject() {
		mainComponent = AppInjector.injectMain(this, supportFragmentManager, R.id.main_container)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		if (savedInstanceState == null) {
			reducer.reduce(ShowSplash)
		}
	}

	override fun onResumeFragments() {
		super.onResumeFragments()
		navigatorHolder.setNavigator(navigator)
	}

	override fun onPause() {
		navigatorHolder.removeNavigator()
		super.onPause()
	}
}
