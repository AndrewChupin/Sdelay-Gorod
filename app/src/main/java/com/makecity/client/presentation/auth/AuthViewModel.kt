package com.makecity.client.presentation.auth

import android.os.Parcelable
import com.makecity.client.app.AppScreens
import com.makecity.client.data.auth.AuthType
import com.makecity.client.domain.auth.AuthInteractor
import com.makecity.core.data.Presentation
import com.makecity.core.domain.Result
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
	val authType: AuthType = AuthType.PHONE,
	val isNextVisible: Boolean = false
) : ViewState


// Action
sealed class AuthAction: ActionView {
	object ShowNextStep: AuthAction()
	data class ResearchContent(
		val content: String
	): AuthAction()
	data class CheckPassword(
		val password: String
	): AuthAction()
	data class CreatePassword(
		val password: String
	): AuthAction()
}


// Data
@Parcelize
data class AuthData(
	val authType: AuthType
): Parcelable


// Reducer
interface AuthReducer: StatementReducer<AuthViewState, AuthAction>


// ViewModel
class AuthViewModel(
	private val router: Router,
	private val authData: AuthData,
	private val authInteractor: AuthInteractor,
	override val connectionProvider: ConnectionProvider,
	override val disposables: CompositeDisposable = CompositeDisposable()
) : BaseViewModel(), AuthReducer, ReducerPluginConnection {

	override val viewState: StateLiveData<AuthViewState>
		= StateLiveData.create(AuthViewState(authType = authData.authType))

	override fun reduce(action: AuthAction) {
		when (action) {
			is AuthAction.ShowNextStep -> showNextStepConsumer(authData.authType)
			is AuthAction.ResearchContent -> researchContentConsumer(action)
			is AuthAction.CheckPassword -> authInteractor
				.checkPassword(action.password)
				.bindSubscribe(onSuccess = {
					router.backTo(AppScreens.MENU_SCREEN_KEY)
				})
		}
	}

	private fun showNextStepConsumer(authType: AuthType) = when (authType) {
		AuthType.PHONE -> router.navigateTo(AppScreens.AUTH_SCREEN_KEY, AuthData(AuthType.PHONE))
		AuthType.SMS -> router.navigateTo(AppScreens.AUTH_SCREEN_KEY, AuthData(AuthType.SMS))
		AuthType.CREATE_PASSWORD -> router.navigateTo(AppScreens.AUTH_SCREEN_KEY, AuthData(AuthType.CREATE_PASSWORD))
		AuthType.PASSWORD -> router.navigateTo(AppScreens.AUTH_SCREEN_KEY, AuthData(AuthType.PASSWORD))
	}

	private fun researchContentConsumer(action: AuthAction.ResearchContent) {
		authInteractor.validateData(action.content, authData.authType)
			.bindSubscribe(onSuccess = { validationResult(it, action.content) })
	}

	private fun validationResult(result: Result<Boolean>, content: CharSequence) = result
		.doIfSuccess { isValid ->
			if (!isValid) {
				return@doIfSuccess
			}

			when (authData.authType) {
				AuthType.PASSWORD -> viewState.updateValue {
					copy(
						isNextVisible = true
					)
				}

				AuthType.CREATE_PASSWORD -> authInteractor
					.createPassword(content.toString())
					.bindSubscribe(onSuccess = {
						router.backTo(AppScreens.MENU_SCREEN_KEY)
					})

				AuthType.SMS -> authInteractor
					.checkSms(content.toString())
					.bindSubscribe(onSuccess = {
						showNextStepConsumer(it.nextStep)
					})

				AuthType.PHONE -> authInteractor
					.sendPhone(content.toString())
					.bindSubscribe(onSuccess = {
						showNextStepConsumer(it.nextStep)
					})
			}


		}

	// IMPLEMENT - ConnectionPlugin
	override fun onChangeConnection(connectionState: ConnectionState) {

	}
}