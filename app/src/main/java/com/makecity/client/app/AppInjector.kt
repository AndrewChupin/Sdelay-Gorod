package com.makecity.client.app

import android.support.v4.app.FragmentManager
import com.makecity.client.di.AppComponent
import com.makecity.client.di.InjectorPlugin
import com.makecity.client.di.MainComponent
import com.makecity.client.presentation.about.AboutFragment
import com.makecity.client.presentation.auth.AuthData
import com.makecity.client.presentation.city.CityFragment
import com.makecity.client.presentation.feed.FeedFragment
import com.makecity.client.presentation.main.MainActivity
import com.makecity.client.presentation.map.MapPointsFragment
import com.makecity.client.presentation.menu.MenuFragment
import com.makecity.client.presentation.notification.NotificationFragment
import com.makecity.client.presentation.auth.AuthFragment
import com.makecity.client.presentation.problem.ProblemData
import com.makecity.client.presentation.problem.ProblemFragment
import com.makecity.client.presentation.profile.ProfileFragment
import com.makecity.client.presentation.splash.SplashFragment
import com.makecity.client.presentation.web.WebData
import com.makecity.client.presentation.web.WebFragment
import java.lang.ref.WeakReference


@Suppress("ReplaceSingleLineLet")
object AppInjector {

	lateinit var appComponent: WeakReference<AppComponent>
	lateinit var mainComponent: WeakReference<MainComponent>

	private lateinit var injectorPlugin: InjectorPlugin

	fun initInjection(injectorPlugin: InjectorPlugin) {
		this.injectorPlugin = injectorPlugin
	}

	fun injectApp(appDelegate: AppDelegate): AppComponent {
		val component = injectorPlugin.representAppComponent(appDelegate)

		component.inject(appDelegate)
		appComponent = WeakReference(component)
		return component
	}

	fun injectMain(mainActivity: MainActivity, fragmentManager: FragmentManager, containerId: Int): MainComponent {
		appComponent.get()?.let {
			val component = injectorPlugin.representMainComponent(it, mainActivity, fragmentManager, containerId)

			component.inject(mainActivity)
			mainComponent = WeakReference(component)
			return component
		}
		throw IllegalStateException("AppComponent must be initialized before MainComponent")
	}

	fun inject(mapPointsFragment: MapPointsFragment) {
		mainComponent.get()?.let {
			injectorPlugin.representMapComponent(it, mapPointsFragment)
				.inject(mapPointsFragment)
			return
		}
		throw IllegalStateException("MainComponent must be initialized before MapPointsComponent")
	}

	fun inject(menuFragment: MenuFragment) {
		mainComponent.get()?.let {
			injectorPlugin.representMenuComponent(it, menuFragment)
				.inject(menuFragment)
			return
		}
		throw IllegalStateException("MainComponent must be initialized before MapPointsComponent")
	}

	fun inject(notificationFragment: NotificationFragment) {
		mainComponent.get()?.let {
			injectorPlugin.representNotificationComponent(it, notificationFragment)
				.inject(notificationFragment)
			return
		}
		throw IllegalStateException("MainComponent must be initialized before NotificationComponent")
	}

	fun inject(feedFragment: FeedFragment) {
		mainComponent.get()?.let {
			injectorPlugin.representFeedFragment(it, feedFragment)
				.inject(feedFragment)
			return
		}
		throw IllegalStateException("MainComponent must be initialized before FeedComponent")
	}

	fun inject(webFragment: WebFragment, webData: WebData) {
		mainComponent.get()?.let {
			injectorPlugin.representWebFragment(it, webFragment, webData)
				.inject(webFragment)
			return
		}
		throw IllegalStateException("MainComponent must be initialized before WebComponent")
	}

	fun inject(fragment: CityFragment) {
		mainComponent.get()?.let {
			injectorPlugin.representCityFragment(it, fragment)
				.inject(fragment)
			return
		}
		throw IllegalStateException("MainComponent must be initialized before CityComponent")
	}

	fun inject(fragment: SplashFragment) {
		mainComponent.get()?.let {
			injectorPlugin.representSplashFragment(it, fragment)
				.inject(fragment)
			return
		}
		throw IllegalStateException("MainComponent must be initialized before CityComponent")
	}

	fun inject(fragment: AboutFragment) {
		mainComponent.get()?.let {
			injectorPlugin.representAboutFragment(it, fragment)
				.inject(fragment)
			return
		}
		throw IllegalStateException("MainComponent must be initialized before AboutComponent")
	}

	fun inject(fragment: ProblemFragment, problemData: ProblemData) {
		mainComponent.get()?.let {
			injectorPlugin.representProblemFragment(it, fragment, problemData)
				.inject(fragment)
			return
		}
		throw IllegalStateException("MainComponent must be initialized before ProblemComponent")
	}

	fun inject(fragment: AuthFragment, authData: AuthData) {
		mainComponent.get()?.let {
			injectorPlugin.representAuthFragment(it, fragment, authData)
				.inject(fragment)
			return
		}
		throw IllegalStateException("MainComponent must be initialized before ProblemComponent")
	}

	fun inject(fragment: ProfileFragment) {
		mainComponent.get()?.let {
			injectorPlugin.representProfileFragment(it, fragment)
				.inject(fragment)
			return
		}
		throw IllegalStateException("MainComponent must be initialized before ProblemComponent")
	}
}
