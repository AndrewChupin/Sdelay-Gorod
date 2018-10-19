package com.makecity.core.presentation.navigation

import android.content.Context
import android.content.Intent
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import com.makecity.core.R
import ru.terrakok.cicerone.android.SupportAppNavigator
import ru.terrakok.cicerone.commands.Command


abstract class BaseNavigator(
        activity: FragmentActivity,
        fragmentManager: FragmentManager,
        @IdRes containerId: Int
): SupportAppNavigator(activity, fragmentManager, containerId) {

    override fun createActivityIntent(context: Context, screenKey: String, data: Any?): Intent? {
        return null
    }

    override fun setupFragmentTransactionAnimation(
            command: Command?,
            currentFragment: Fragment?,
            nextFragment: Fragment?,
            fragmentTransaction: FragmentTransaction?
    ) {
        currentFragment?.let {
           /* fragmentTransaction?.setCustomAnimations(
                R.anim.enter_scale,
                R.anim.exit_scale,
                R.anim.pop_enter_scale,
                R.anim.pop_exit_scale
            )*/
        }
    }
}
