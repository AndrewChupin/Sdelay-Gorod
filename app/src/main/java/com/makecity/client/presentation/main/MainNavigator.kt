package com.makecity.client.presentation.main

import android.content.Context
import android.content.Intent
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import com.makecity.client.app.AppScreens
import com.makecity.client.presentation.about.AboutFragment
import com.makecity.client.presentation.address.AddressFragment
import com.makecity.client.presentation.auth.AuthData
import com.makecity.client.presentation.auth.AuthFragment
import com.makecity.client.presentation.camera.CameraFragment
import com.makecity.client.presentation.camera.CameraScreenData
import com.makecity.client.presentation.category.CategoryScreenData
import com.makecity.client.presentation.category.CategoryFragment
import com.makecity.client.presentation.city.CityFragment
import com.makecity.client.presentation.create_problem.CreateProblemData
import com.makecity.client.presentation.create_problem.CreateProblemFragment
import com.makecity.client.presentation.description.DescriptionFragment
import com.makecity.client.presentation.description.DescriptionScreenData
import com.makecity.client.presentation.edit_profile.EditProfileFragment
import com.makecity.client.presentation.feed.FeedFragment
import com.makecity.client.presentation.filter.ProblemFilterFragment
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
import com.makecity.core.R
import com.makecity.core.presentation.navigation.BaseNavigator
import ru.terrakok.cicerone.commands.Command
import ru.terrakok.cicerone.commands.Forward
import javax.inject.Inject


class MainNavigator @Inject constructor(
	private val activity: FragmentActivity,
	fragmentManager: FragmentManager,
	@IdRes containerId: Int
): BaseNavigator(activity, fragmentManager, containerId) {

	companion object {
		private const val PHOTO_REQ_CODE = 533
	}

	override fun createFragment(screenKey: String?, data: Any?): Fragment {
		return when (screenKey) {
			AppScreens.SPLASH_SCREEN_KEY -> SplashFragment.newInstance()
			AppScreens.MAP_SCREEN_KEY -> MapPointsFragment.newInstance()
			AppScreens.MENU_SCREEN_KEY -> MenuFragment.newInstance()
			AppScreens.NOTIFICATION_SCREEN_KEY -> NotificationFragment.newInstance()
			AppScreens.FEED_SCREEN_KEY -> FeedFragment.newInstance()
			AppScreens.ABOUT_SCREEN_KEY -> AboutFragment.newInstance()
			AppScreens.CITY_SCREEN_KEY -> CityFragment.newInstance()
			AppScreens.PROFILE_SCREEN_KEY -> ProfileFragment.newInstance()
			AppScreens.EDIT_PROFILE_SCREEN_KEY -> EditProfileFragment.newInstance()
			AppScreens.ADDRESS_SCREEN_KEY -> AddressFragment.newInstance()
			AppScreens.CREATE_PROBLEM_SCREEN_KEY -> {
				if (data != null && data !is CreateProblemData) {
					throw IllegalArgumentException("data is null or type not CreateProblemData")
				}
				CreateProblemFragment.newInstance(data as CreateProblemData)
			}
			AppScreens.EDIT_PROBLEM_SCREEN_KEY -> EditProfileFragment.newInstance()
			AppScreens.FILTER_PROBLEM_SCREEN_KEY -> ProblemFilterFragment.newInstance()
			AppScreens.RESTORE_SCREEN_KEY -> RestoreFragment.newInstance()
			AppScreens.MAP_ADDRESS_SCREEN_KEY -> {
				if (data != null && data !is MapAddressScreenData) {
					throw IllegalArgumentException("data is null or type not MapAddressScreenData")
				}
				MapAddressFragment.newInstance(data as MapAddressScreenData)
			}
			AppScreens.OWN_PROBLEMS_SCREEN_KEY -> OwnProblemsFragment.newInstance()
			AppScreens.SETTINGS_SCREEN_KEY -> SettingsFragment.newInstance()
			AppScreens.CAMERA_SCREEN_KEY -> {
				if (data != null && data !is CameraScreenData) {
					throw IllegalArgumentException("data is null or type not CameraScreenData")
				}
				CameraFragment.newInstance(data as CameraScreenData)
			}
			AppScreens.DESCRIPTION_SCREEN_KEY -> {
				if (data != null && data !is DescriptionScreenData) {
					throw IllegalArgumentException("data is null or type not DescriptionScreenData")
				}
				DescriptionFragment.newInstance(data as DescriptionScreenData)
			}
			AppScreens.CATEGORY_SCREEN_KEY -> {
				if (data != null && data !is CategoryScreenData) {
					throw IllegalArgumentException("data is null or type not CategoryScreenData")
				}
				CategoryFragment.newInstance(data as CategoryScreenData)
			}
			AppScreens.AUTH_SCREEN_KEY ->  {
				if (data != null && data !is AuthData) {
					throw IllegalArgumentException("data is null or type not AuthData")
				}
				AuthFragment.newInstance(data as AuthData)
			}
			AppScreens.PROBLEM_SCREEN_KEY -> {
				if (data != null && data !is ProblemData) {
					throw IllegalArgumentException("data is null or type not ProblemData")
				}
				ProblemFragment.newInstance(data as ProblemData)
			}
			AppScreens.WEB_SCREEN_KEY -> {
				if (data != null && data !is WebData) {
					throw IllegalArgumentException("data is null or type not WebData")
				}
				WebFragment.newInstance(data as WebData)
			}
			else -> throw IllegalArgumentException("Unsupported screen with key $screenKey")
		}
	}

	override fun setupFragmentTransactionAnimation(
		command: Command?,
		currentFragment: Fragment?,
		nextFragment: Fragment?,
		fragmentTransaction: FragmentTransaction?
	) {
		if (nextFragment is AuthFragment) {
			currentFragment?.let {
				fragmentTransaction?.setCustomAnimations(
					R.anim.enter_right_to_left,
					R.anim.exit_right_to_left,
					R.anim.pop_enter_left_to_right,
					R.anim.pop_exit_left_to_right
				)
			}
		} else {
			super.setupFragmentTransactionAnimation(command, currentFragment, nextFragment, fragmentTransaction)
		}
	}

	override fun createActivityIntent(context: Context, screenKey: String, data: Any?): Intent? {
		if (screenKey == AppScreens.IMAGE_PICKER_SCREEN_KEY) {
			val intent = Intent(Intent.ACTION_PICK)
			intent.type = "image/*"
			return intent
		}
		return super.createActivityIntent(context, screenKey, data)
	}


	override fun forward(command: Forward?) {
		if (command != null && command.screenKey == AppScreens.IMAGE_PICKER_SCREEN_KEY) {
			val activityIntent = createActivityIntent(activity, command.screenKey, command.transitionData)
			activity.startActivityForResult(activityIntent, PHOTO_REQ_CODE)
			return
		}

		super.forward(command)
	}
}
