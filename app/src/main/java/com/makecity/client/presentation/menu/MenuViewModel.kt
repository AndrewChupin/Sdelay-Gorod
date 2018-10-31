package com.makecity.client.presentation.menu

import com.makecity.client.BuildConfig
import com.makecity.client.app.AppScreens
import com.makecity.client.data.auth.AuthType
import com.makecity.client.data.auth.TokenNotFounded
import com.makecity.client.data.profile.Profile
import com.makecity.client.data.profile.ProfileDataSource
import com.makecity.client.presentation.auth.AuthData
import com.makecity.client.presentation.web.WebData
import com.makecity.core.data.Presentation
import com.makecity.core.plugin.connection.ConnectionProvider
import com.makecity.core.plugin.connection.ConnectionState
import com.makecity.core.plugin.connection.ReducerPluginConnection
import com.makecity.core.presentation.state.PrimaryViewState
import com.makecity.core.presentation.state.StateLiveData
import com.makecity.core.presentation.state.ViewState
import com.makecity.core.presentation.viewmodel.ActionView
import com.makecity.core.presentation.viewmodel.BaseViewModel
import com.makecity.core.presentation.viewmodel.StatementReducer
import com.makecity.core.utils.Symbols.EMPTY
import io.reactivex.disposables.CompositeDisposable
import ru.terrakok.cicerone.Router


// State
@Presentation
data class MenuViewState(
	override val screenState: PrimaryViewState = PrimaryViewState.Loading,
	val profile: Profile? = null
) : ViewState


// Action
sealed class MenuAction: ActionView {
	data class ItemSelected(val item: MenuType): MenuAction()
	object ShowProfile: MenuAction()
	object RefreshProfileData: MenuAction()
}

enum class MenuType {
	SETTINGS, PARTNERS, SUPPORT, ARCHIVE, HELP, ABOUT_PROJECT, NOTIFICATIONS
}

// Reducer
interface MenuReducer: StatementReducer<MenuViewState, MenuAction>


// ViewModel
class MenuViewModel(
	private val router: Router,
	private val profileDataSource: ProfileDataSource,
	override val connectionProvider: ConnectionProvider,
	override val disposables: CompositeDisposable = CompositeDisposable()
) : BaseViewModel(), MenuReducer, ReducerPluginConnection {

	override val viewState: StateLiveData<MenuViewState> = StateLiveData.create(MenuViewState())

	override fun reduce(action: MenuAction) {
		when (action) {
			is MenuAction.ItemSelected -> reduceItem(action.item)
			is MenuAction.ShowProfile -> {
				if (state.profile == null) {
					router.navigateTo(AppScreens.AUTH_SCREEN_KEY, AuthData(AuthType.PHONE))
				} else {
					router.navigateTo(AppScreens.PROFILE_SCREEN_KEY)
				}
			}
			is MenuAction.RefreshProfileData -> onProfileLoaded()
		}
	}

	private fun onProfileLoaded() {
		viewState.updateValue { copy(screenState = PrimaryViewState.Loading, profile = null) }

		profileDataSource
			.refreshProfile()
			.bindSubscribe(onSuccess = {
				viewState.updateValue {
					copy(screenState = PrimaryViewState.Data, profile = it)
				}
			}, onError = {
				viewState.updateValue {
					copy(screenState = PrimaryViewState.Data, profile = null)
				}
			})
	}

	private fun reduceItem(type: MenuType) = when (type) {
		MenuType.SETTINGS -> {}
		MenuType.PARTNERS -> router.navigateTo(AppScreens.WEB_SCREEN_KEY,
			WebData(BuildConfig.PARTNERS_URL, EMPTY))
		MenuType.SUPPORT -> router.navigateTo(AppScreens.WEB_SCREEN_KEY,
			WebData(BuildConfig.SUPPORT_URL, EMPTY))
		MenuType.ARCHIVE -> {}
		MenuType.HELP -> {}
		MenuType.ABOUT_PROJECT -> router.navigateTo(AppScreens.ABOUT_SCREEN_KEY)
		MenuType.NOTIFICATIONS -> router.navigateTo(AppScreens.NOTIFICATION_SCREEN_KEY)
	}

	// IMPLEMENT - ConnectionPlugin
	override fun onChangeConnection(connectionState: ConnectionState) {

	}
}