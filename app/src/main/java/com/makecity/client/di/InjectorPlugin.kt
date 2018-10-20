package com.makecity.client.di

import android.support.v4.app.FragmentManager
import com.makecity.client.app.AppDelegate
import com.makecity.client.presentation.about.AboutFragment
import com.makecity.client.presentation.auth.AuthData
import com.makecity.client.presentation.city.CityFragment
import com.makecity.client.presentation.feed.FeedFragment
import com.makecity.client.presentation.main.MainActivity
import com.makecity.client.presentation.map.MapPointsFragment
import com.makecity.client.presentation.menu.MenuFragment
import com.makecity.client.presentation.notification.NotificationFragment
import com.makecity.client.presentation.auth.AuthFragment
import com.makecity.client.presentation.edit_profile.EditProfileFragment
import com.makecity.client.presentation.problem.ProblemData
import com.makecity.client.presentation.problem.ProblemFragment
import com.makecity.client.presentation.profile.ProfileFragment
import com.makecity.client.presentation.splash.SplashFragment
import com.makecity.client.presentation.web.WebData
import com.makecity.client.presentation.web.WebFragment


interface InjectorPlugin {

	fun representAppComponent(
		appDelegate: AppDelegate
	): AppComponent

	fun representMainComponent(
		appComponent: AppComponent,
		mainActivity: MainActivity,
		fragmentManager: FragmentManager,
		containerId: Int
	): MainComponent

	fun representMapComponent(
		mainComponent: MainComponent,
		mapPointsFragment: MapPointsFragment
	): MapPointsComponent


	fun representMenuComponent(
		mainComponent: MainComponent,
		menuFragment: MenuFragment
	): MenuComponent

	fun representNotificationComponent(
		mainComponent: MainComponent,
		notificationsFragment: NotificationFragment
	): NotificationComponent

	fun representFeedFragment(
		mainComponent: MainComponent,
		feedFragment: FeedFragment
	): FeedComponent

	fun representWebFragment(
		mainComponent: MainComponent,
		webFragment: WebFragment,
		webData: WebData
	): WebComponent

	fun representCityFragment(
		mainComponent: MainComponent,
		fragment: CityFragment
	): CityComponent

	fun representSplashFragment(
		mainComponent: MainComponent,
		fragment: SplashFragment
	): SplashComponent

	fun representAboutFragment(
		mainComponent: MainComponent,
		fragment: AboutFragment
	): AboutComponent

	fun representProblemFragment(
		mainComponent: MainComponent,
		fragment: ProblemFragment,
		problemData: ProblemData
	): ProblemComponent

	fun representAuthFragment(
		mainComponent: MainComponent,
		fragment: AuthFragment,
		authData: AuthData
	): AuthComponent

	fun representProfileFragment(
		mainComponent: MainComponent,
		fragment: ProfileFragment
	): ProfileComponent

	fun representEditProfileFragment(
		mainComponent: MainComponent,
		fragment: EditProfileFragment
	): EditProfileComponent
}
