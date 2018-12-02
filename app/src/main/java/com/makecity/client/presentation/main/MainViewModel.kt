package com.makecity.client.presentation.main


import com.makecity.client.app.AppScreens
import com.makecity.core.presentation.viewmodel.ActionView
import com.makecity.core.presentation.viewmodel.BaseReducer
import com.makecity.core.presentation.viewmodel.BaseViewModel
import ru.terrakok.cicerone.Router
import javax.inject.Inject


// Actions
sealed class MainAction : ActionView

object ShowFeedAction : MainAction()
object ShowCityAction : MainAction()
object ShowSplash : MainAction()
object ShowNotificationsAction : MainAction()
object ShowMenuAction : MainAction()


// Reducer
interface MainReducer : BaseReducer<MainAction>


// ViewModel
class MainViewModel @Inject constructor(
	private val router: Router
) : BaseViewModel(), MainReducer {

	override fun reduce(action: MainAction) = when (action) {
		is ShowCityAction -> router.newRootScreen(AppScreens.CITY_SCREEN_KEY)
		is ShowFeedAction -> router.newRootScreen(AppScreens.FEED_SCREEN_KEY)
		is ShowSplash -> router.newRootScreen(AppScreens.SPLASH_SCREEN_KEY)
		is ShowNotificationsAction -> router.newRootScreen(AppScreens.NOTIFICATION_SCREEN_KEY)
		is ShowMenuAction -> router.newRootScreen(AppScreens.MENU_SCREEN_KEY)
	}
}