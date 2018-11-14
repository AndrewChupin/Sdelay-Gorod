package com.makecity.client.app

import android.support.v4.app.FragmentManager
import com.makecity.client.di.AppComponent
import com.makecity.client.di.MainComponent
import com.makecity.client.di.common.InjectorPlugin
import com.makecity.client.presentation.about.AboutFragment
import com.makecity.client.presentation.address.AddressFragment
import com.makecity.client.presentation.auth.AuthData
import com.makecity.client.presentation.auth.AuthFragment
import com.makecity.client.presentation.camera.CameraFragment
import com.makecity.client.presentation.camera.CameraScreenData
import com.makecity.client.presentation.category.CategoryScreenData
import com.makecity.client.presentation.category.CategoryFragment
import com.makecity.client.presentation.city.CityFragment
import com.makecity.client.presentation.comments.CommentsFragment
import com.makecity.client.presentation.create_problem.CreateProblemData
import com.makecity.client.presentation.create_problem.CreateProblemFragment
import com.makecity.client.presentation.description.DescriptionFragment
import com.makecity.client.presentation.description.DescriptionScreenData
import com.makecity.client.presentation.edit_problem.EditProblemFragment
import com.makecity.client.presentation.edit_profile.EditProfileFragment
import com.makecity.client.presentation.feed.FeedFragment
import com.makecity.client.presentation.filter.ProblemFilterFragment
import com.makecity.client.presentation.main.MainActivity
import com.makecity.client.presentation.map.MapPointsFragment
import com.makecity.client.presentation.map_address.MapAddressFragment
import com.makecity.client.presentation.map_address.MapAddressScreenData
import com.makecity.client.presentation.menu.MenuFragment
import com.makecity.client.presentation.notification.NotificationFragment
import com.makecity.client.presentation.own_problems.OwnProblemsFragment
import com.makecity.client.presentation.problem.ProblemData
import com.makecity.client.presentation.problem.ProblemFragment
import com.makecity.client.presentation.profile.ProfileFragment
import com.makecity.client.presentation.restore.RestoreFragment
import com.makecity.client.presentation.settings.SettingsFragment
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
		throw IllegalStateException("MainComponent must be initialized before AuthComponent")
	}

	fun inject(fragment: ProfileFragment) {
		mainComponent.get()?.let {
			injectorPlugin.representProfileFragment(it, fragment)
				.inject(fragment)
			return
		}
		throw IllegalStateException("MainComponent must be initialized before ProfileComponent")
	}

	fun inject(fragment: EditProfileFragment) {
		mainComponent.get()?.let {
			injectorPlugin.representEditProfileFragment(it, fragment)
				.inject(fragment)
			return
		}
		throw IllegalStateException("MainComponent must be initialized before EditProfileComponent")
	}

	fun inject(fragment: AddressFragment) {
		mainComponent.get()?.let {
			injectorPlugin.representAddressFragment(it, fragment)
				.inject(fragment)
			return
		}
		throw IllegalStateException("MainComponent must be initialized before AddressComponentRemote")
	}

	fun inject(fragment: CreateProblemFragment, data: CreateProblemData) {
		mainComponent.get()?.let {
			injectorPlugin.representCreateProblemFragment(it, fragment, data)
				.inject(fragment)
			return
		}
		throw IllegalStateException("MainComponent must be initialized before CreateProblemComponent")
	}

	fun inject(fragment: MapAddressFragment, data: MapAddressScreenData) {
		mainComponent.get()?.let {
			injectorPlugin.representMapAddressFragment(it, fragment, data)
				.inject(fragment)
			return
		}
		throw IllegalStateException("MainComponent must be initialized before MapAddressComponent")
	}

	fun inject(fragment: OwnProblemsFragment) {
		mainComponent.get()?.let {
			injectorPlugin.representOwnProblemsFragment(it, fragment)
				.inject(fragment)
			return
		}
		throw IllegalStateException("MainComponent must be initialized before OwnProblemsComponent")
	}

	fun inject(fragment: ProblemFilterFragment) {
		mainComponent.get()?.let {
			injectorPlugin.representProblemFilterFragment(it, fragment)
				.inject(fragment)
			return
		}
		throw IllegalStateException("MainComponent must be initialized before ProblemFilterComponent")
	}

	fun inject(fragment: EditProblemFragment) {
		mainComponent.get()?.let {
			injectorPlugin.representEditProblemFragment(it, fragment)
				.inject(fragment)
			return
		}
		throw IllegalStateException("MainComponent must be initialized before SettingsComponent")
	}

	fun inject(fragment: SettingsFragment) {
		mainComponent.get()?.let {
			injectorPlugin.representSettingsFragment(it, fragment)
				.inject(fragment)
			return
		}
		throw IllegalStateException("MainComponent must be initialized before SettingsComponent")
	}

	fun inject(fragment: CameraFragment, data: CameraScreenData) {
		mainComponent.get()?.let {
			injectorPlugin.representCameraFragment(it, fragment, data)
				.inject(fragment)
			return
		}
		throw IllegalStateException("MainComponent must be initialized before CameraComponent")
	}

	fun inject(fragment: CategoryFragment, categoryData: CategoryScreenData) {
		mainComponent.get()?.let {
			injectorPlugin.representCategoryFragment(it, fragment, categoryData)
				.inject(fragment)
			return
		}
		throw IllegalStateException("MainComponent must be initialized before CategoryComponent")
	}

	fun inject(fragment: DescriptionFragment, descriptionScreenData: DescriptionScreenData) {
		mainComponent.get()?.let {
			injectorPlugin.representDescriptionFragment(it, fragment, descriptionScreenData)
				.inject(fragment)
			return
		}
		throw IllegalStateException("MainComponent must be initialized before DescriptionComponent")
	}

	fun inject(fragment: RestoreFragment) {
		mainComponent.get()?.let {
			injectorPlugin.representRestoreFragment(it, fragment)
				.inject(fragment)
			return
		}
		throw IllegalStateException("MainComponent must be initialized before DescriptionComponent")
	}

	fun inject(fragment: CommentsFragment) {
		mainComponent.get()?.let {
			injectorPlugin.representCommentsFragment(it, fragment)
				.inject(fragment)
			return
		}
		throw IllegalStateException("MainComponent must be initialized before CommentsComponent")
	}
}
