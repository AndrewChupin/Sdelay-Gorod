package com.makecity.client.presentation.menu

import com.makecity.client.BuildConfig
import com.makecity.client.app.AppScreens
import com.makecity.client.presentation.auth.AuthData
import com.makecity.client.presentation.auth.AuthType
import com.makecity.client.presentation.web.WebData
import com.makecity.core.data.Presentation
import com.makecity.core.plugin.connection.ConnectionProvider
import com.makecity.core.plugin.connection.ConnectionState
import com.makecity.core.plugin.connection.ReducerPluginConnection
import com.makecity.core.presentation.state.PrimaryViewState
import com.makecity.core.presentation.state.ViewState
import com.makecity.core.presentation.state.StateLiveData
import com.makecity.core.presentation.viewmodel.ActionView
import com.makecity.core.presentation.viewmodel.BaseViewModel
import com.makecity.core.presentation.viewmodel.StatementReducer
import com.makecity.core.utils.Symbols.EMPTY
import io.reactivex.disposables.CompositeDisposable
import ru.terrakok.cicerone.Router


// State
@Presentation
data class MenuViewState(
	override val screenState: PrimaryViewState = PrimaryViewState.Loading
) : ViewState


// Action
sealed class MenuAction: ActionView {
	data class ItemSelected(val item: MenuType): MenuAction()
}

enum class MenuType {
	ADD_ACCOUNT, SETTINGS, PARTNERS, SUPPORT, ARCHIVE, HELP, ABOUT_PROJECT
}

// Reducer
interface MenuReducer: StatementReducer<MenuViewState, MenuAction>


// ViewModel
class MenuViewModel(
	private val router: Router,
	override val connectionProvider: ConnectionProvider,
	override val disposables: CompositeDisposable = CompositeDisposable()
) : BaseViewModel(), MenuReducer, ReducerPluginConnection {

	override val viewState: StateLiveData<MenuViewState> = StateLiveData.create(MenuViewState())

	override fun reduce(action: MenuAction) = when (action) {
		is MenuAction.ItemSelected -> reduceItem(action.item)
	}

	private fun reduceItem(type: MenuType) = when (type) {
		MenuType.ADD_ACCOUNT -> { router.navigateTo(AppScreens.AUTH_SCREEN_KEY, AuthData(AuthType.PHONE)) }
		MenuType.SETTINGS -> {}
		MenuType.PARTNERS -> router.navigateTo(AppScreens.WEB_SCREEN_KEY,
			WebData(BuildConfig.PARTNERS_URL, EMPTY))
		MenuType.SUPPORT -> router.navigateTo(AppScreens.WEB_SCREEN_KEY,
			WebData(BuildConfig.SUPPORT_URL, EMPTY))
		MenuType.ARCHIVE -> {}
		MenuType.HELP -> router.navigateTo(AppScreens.PROFILE_SCREEN_KEY)
		MenuType.ABOUT_PROJECT -> router.navigateTo(AppScreens.ABOUT_SCREEN_KEY)
	}

	// IMPLEMENT - ConnectionPlugin
	override fun onChangeConnection(connectionState: ConnectionState) {

	}
}