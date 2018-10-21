package com.makecity.client.di.common

import android.support.v4.app.FragmentManager
import com.makecity.client.app.AppDelegate
import com.makecity.client.di.*
import com.makecity.client.presentation.about.AboutFragment
import com.makecity.client.presentation.address.AddressFragment
import com.makecity.client.presentation.auth.AuthData
import com.makecity.client.presentation.city.CityFragment
import com.makecity.client.presentation.feed.FeedFragment
import com.makecity.client.presentation.main.MainActivity
import com.makecity.client.presentation.map.MapPointsFragment
import com.makecity.client.presentation.menu.MenuFragment
import com.makecity.client.presentation.notification.NotificationFragment
import com.makecity.client.presentation.auth.AuthFragment
import com.makecity.client.presentation.create_problem.CreateProblemFragment
import com.makecity.client.presentation.edit_problem.EditProblemFragment
import com.makecity.client.presentation.edit_profile.EditProfileFragment
import com.makecity.client.presentation.filter.ProblemFilterFragment
import com.makecity.client.presentation.map_address.MapAddressFragment
import com.makecity.client.presentation.own_problems.OwnProblemsFragment
import com.makecity.client.presentation.problem.ProblemData
import com.makecity.client.presentation.problem.ProblemFragment
import com.makecity.client.presentation.profile.ProfileFragment
import com.makecity.client.presentation.settings.SettingsFragment
import com.makecity.client.presentation.splash.SplashFragment
import com.makecity.client.presentation.web.WebData
import com.makecity.client.presentation.web.WebFragment


object DefaultInjectorPlugin : InjectorPlugin {

	override fun representAppComponent(
		appDelegate: AppDelegate
	): AppComponent = DaggerAppComponent.builder()
		.withApplication(appDelegate)
		.build()

	override fun representMainComponent(
		appComponent: AppComponent,
		mainActivity: MainActivity,
		fragmentManager: FragmentManager,
		containerId: Int
	): MainComponent = appComponent
		.mainComponent()
		.withActivity(mainActivity)
		.withFragmentManager(fragmentManager)
		.withContainer(containerId)
		.build()

	override fun representMapComponent(
		mainComponent: MainComponent,
		mapPointsFragment: MapPointsFragment
	): MapPointsComponent = mainComponent
		.mapPointsComponent()
		.withFragment(mapPointsFragment)
		.build()

	override fun representMenuComponent(
		mainComponent: MainComponent,
		menuFragment: MenuFragment
	): MenuComponent = mainComponent
		.menuComponent()
		.withFragment(menuFragment)
		.build()

	override fun representNotificationComponent(
		mainComponent: MainComponent,
		notificationsFragment: NotificationFragment
	): NotificationComponent = mainComponent
		.notificationComponent()
		.withFragment(notificationsFragment)
		.build()

	override fun representFeedFragment(
		mainComponent: MainComponent,
		feedFragment: FeedFragment
	): FeedComponent = mainComponent
		.feedComponent()
		.withFragment(feedFragment)
		.build()

	override fun representWebFragment(
		mainComponent: MainComponent,
		webFragment: WebFragment,
		webData: WebData
	): WebComponent = mainComponent
		.webComponent()
		.withFragment(webFragment)
		.with(webData)
		.build()

	override fun representCityFragment(
		mainComponent: MainComponent,
		fragment: CityFragment
	): CityComponent = mainComponent
		.cityComponent()
		.withFragment(fragment)
		.build()

	override fun representSplashFragment(
		mainComponent: MainComponent,
		fragment: SplashFragment
	): SplashComponent = mainComponent
		.splashComponent()
		.withFragment(fragment)
		.build()

	override fun representAboutFragment(
		mainComponent: MainComponent,
		fragment: AboutFragment
	): AboutComponent = mainComponent
		.aboutComponent()
		.withFragment(fragment)
		.build()

	override fun representProblemFragment(
		mainComponent: MainComponent,
		fragment: ProblemFragment,
		problemData: ProblemData
	): ProblemComponent = mainComponent
		.problemComponent()
		.withData(problemData)
		.withFragment(fragment)
		.build()

	override fun representAuthFragment(
		mainComponent: MainComponent,
		fragment: AuthFragment,
		authData: AuthData
	): AuthComponent = mainComponent
		.phoneComponent()
		.withFragment(fragment)
		.withData(authData)
		.build()

	override fun representProfileFragment(
		mainComponent: MainComponent,
		fragment: ProfileFragment
	): ProfileComponent = mainComponent
		.profileComponent()
		.withFragment(fragment)
		.build()

	override fun representEditProfileFragment(
		mainComponent: MainComponent,
		fragment: EditProfileFragment
	): EditProfileComponent = mainComponent
		.editProfileComponent()
		.withFragment(fragment)
		.build()

	override fun representAddressFragment(
		mainComponent: MainComponent,
		fragment: AddressFragment
	): AddressComponent = mainComponent
		.addressComponent()
		.withFragment(fragment)
		.build()

	override fun representCreateProblemFragment(
		mainComponent: MainComponent,
		fragment: CreateProblemFragment
	): CreateProblemComponent = mainComponent
		.createProblemComponent()
		.withFragment(fragment)
		.build()

	override fun representEditProblemFragment(
		mainComponent: MainComponent,
		fragment: EditProblemFragment
	): EditProblemComponent = mainComponent
		.editProblemComponent()
		.withFragment(fragment)
		.build()

	override fun representMapAddressFragment(
		mainComponent: MainComponent,
		fragment: MapAddressFragment
	): MapAddressComponent = mainComponent
		.mapAddressComponent()
		.withFragment(fragment)
		.build()

	override fun representOwnProblemsFragment(
		mainComponent: MainComponent,
		fragment: OwnProblemsFragment
	): OwnProblemsComponent = mainComponent
		.ownProblemsComponent()
		.withFragment(fragment)
		.build()

	override fun representProblemFilterFragment(
		mainComponent: MainComponent,
		fragment: ProblemFilterFragment
	): ProblemFilterComponent = mainComponent
		.problemFilterComponent()
		.withFragment(fragment)
		.build()

	override fun representSettingsFragment(
		mainComponent: MainComponent,
		fragment: SettingsFragment
	): SettingsComponent = mainComponent
		.settingsComponent()
		.withFragment(fragment)
		.build()
}