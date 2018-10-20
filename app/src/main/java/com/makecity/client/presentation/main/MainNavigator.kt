package com.makecity.client.presentation.main

import android.content.Context
import android.content.Intent
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import com.makecity.client.app.AppScreens
import com.makecity.client.presentation.about.AboutFragment
import com.makecity.client.presentation.auth.AuthData
import com.makecity.client.presentation.city.CityFragment
import com.makecity.client.presentation.feed.FeedFragment
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
import com.makecity.core.presentation.navigation.BaseNavigator
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
		}

		super.forward(command)
	}
}
