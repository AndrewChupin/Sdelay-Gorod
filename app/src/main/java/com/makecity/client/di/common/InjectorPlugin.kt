package com.makecity.client.di.common

import android.support.v4.app.FragmentManager
import com.makecity.client.app.AppDelegate
import com.makecity.client.di.*
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

	fun representAddressFragment(
		mainComponent: MainComponent,
		fragment: AddressFragment
	): AddressComponent

	fun representCreateProblemFragment(
		mainComponent: MainComponent,
		fragment: CreateProblemFragment,
		data: CreateProblemData
	): CreateProblemComponent

	fun representEditProblemFragment(
		mainComponent: MainComponent,
		fragment: EditProblemFragment
	): EditProblemComponent

	fun representMapAddressFragment(
		mainComponent: MainComponent,
		fragment: MapAddressFragment,
		data: MapAddressScreenData
	): MapAddressComponent

	fun representOwnProblemsFragment(
		mainComponent: MainComponent,
		fragment: OwnProblemsFragment
	): OwnProblemsComponent

	fun representProblemFilterFragment(
		mainComponent: MainComponent,
		fragment: ProblemFilterFragment
	): ProblemFilterComponent

	fun representSettingsFragment(
		mainComponent: MainComponent,
		fragment: SettingsFragment
	): SettingsComponent

	fun representCameraFragment(
		mainComponent: MainComponent,
		fragment: CameraFragment,
		data: CameraScreenData
	): CameraComponent

	fun representCategoryFragment(
		mainComponent: MainComponent,
		fragment: CategoryFragment,
		categoryData: CategoryScreenData
	): CategoryComponent

	fun representDescriptionFragment(
		mainComponent: MainComponent,
		fragment: DescriptionFragment,
		descriptionScreenData: DescriptionScreenData
	): DescriptionComponent

	fun representRestoreFragment(
		mainComponent: MainComponent,
		fragment: RestoreFragment
	): RestoreComponent

	fun representCommentsFragment(
		mainComponent: MainComponent,
		fragment: CommentsFragment
	): CommentsComponent
}
