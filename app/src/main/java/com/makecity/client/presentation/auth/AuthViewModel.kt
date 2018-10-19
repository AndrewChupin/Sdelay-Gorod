package com.makecity.client.presentation.auth

import android.os.Parcelable
import com.makecity.client.app.AppScreens
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
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.parcel.Parcelize
import ru.terrakok.cicerone.Router

// State
@Presentation
data class AuthViewState(
	override val screenState: PrimaryViewState = PrimaryViewState.Loading,
	val authType: AuthType = AuthType.PHONE
) : ViewState


// Action
sealed class AuthAction: ActionView
object ShowNextStep: AuthAction()


// Data
@Parcelize
data class AuthData(
	val authType: AuthType
): Parcelable

enum class AuthType {
	PHONE, SMS, PASSWORD, CREATE_PASSWORD
}


// Reducer
interface AuthReducer: StatementReducer<AuthViewState, AuthAction>


// ViewModel
class AuthViewModel(
	private val router: Router,
	private val authData: AuthData,
	override val connectionProvider: ConnectionProvider,
	override val disposables: CompositeDisposable = CompositeDisposable()
) : BaseViewModel(), AuthReducer, ReducerPluginConnection {

	override val viewState: StateLiveData<AuthViewState>
		= StateLiveData.create(AuthViewState(authType = authData.authType))

	override fun reduce(action: AuthAction) {
		if (action is ShowNextStep) {
			when (authData.authType) {
				AuthType.PHONE -> router.navigateTo(AppScreens.AUTH_SCREEN_KEY, AuthData(AuthType.SMS))
				AuthType.SMS -> router.navigateTo(AppScreens.AUTH_SCREEN_KEY, AuthData(AuthType.PASSWORD))
				AuthType.PASSWORD -> router.navigateTo(AppScreens.AUTH_SCREEN_KEY, AuthData(AuthType.CREATE_PASSWORD))
				AuthType.CREATE_PASSWORD -> router.backTo(AppScreens.MENU_SCREEN_KEY)
			}
		}
	}

	// IMPLEMENT - ConnectionPlugin
	override fun onChangeConnection(connectionState: ConnectionState) {

	}
}