package com.makecity.client.presentation.auth

import android.os.Parcelable
import com.makecity.client.app.AppConst.SECURE_RETRY_TIMEOUT
import com.makecity.client.app.AppScreens
import com.makecity.client.data.auth.AuthType
import com.makecity.client.domain.auth.AuthInteractor
import com.makecity.core.data.Presentation
import com.makecity.core.domain.Result
import com.makecity.core.plugin.channel.DefaultMessage
import com.makecity.core.plugin.channel.ReducerPluginChannel
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
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.parcel.Parcelize
import ru.terrakok.cicerone.Router
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger


// State
@Presentation
data class AuthViewState(
	override val screenState: PrimaryViewState = PrimaryViewState.Data,
	val authType: AuthType = AuthType.PHONE,
	val authPhone: String = EMPTY,
	val isPassRepeated: Boolean = false,
	val blockingSeconds: Int = 0,
	val isResetContent: Boolean = false
) : ViewState


// Action
sealed class AuthAction: ActionView {
	object ShowNextStep : AuthAction()
	object RefreshSms : AuthAction()
	object BackClick : AuthAction()
	data class ResearchContent(
		val content: String
	) : AuthAction()
	data class CheckPassword(
		val password: String
	) : AuthAction()
	data class CreatePassword(
		val password: String
	) : AuthAction()
}


// Data
@Parcelize
data class AuthData(
	val authType: AuthType
): Parcelable


// Reducer
interface AuthReducer: StatementReducer<AuthViewState, AuthAction>, ReducerPluginChannel<DefaultMessage>


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

	override var channel: ((DefaultMessage) -> Unit)? = null

	private var lastContent = EMPTY
	private var rememberedPassword = EMPTY
	private val atomicInteger = AtomicInteger(SECURE_RETRY_TIMEOUT)
	private var timerDisposable: Disposable? = null

	init {
		if (authData.authType == AuthType.SMS) {
			startTimer()
			updatePhone()
		}
	}

	// MARK - Common Actions
	override fun reduce(action: AuthAction) {
		when (action) {
			is AuthAction.ResearchContent -> researchContentConsumer(action)
			is AuthAction.ShowNextStep -> showNextStepOnButton(authData.authType)
			is AuthAction.RefreshSms -> onRefreshSms()
			is AuthAction.CheckPassword -> checkPassword(action.password)
			is AuthAction.CreatePassword -> if (rememberedPassword.isEmpty()) {
				onRememberPassword(action.password)
			} else {
				onRepeatPassword(action.password)
			}
		}
	}

	private fun showNextStepByType(nextAuthType: AuthType) = when (nextAuthType) {
		AuthType.PHONE -> router.navigateTo(AppScreens.AUTH_SCREEN_KEY, AuthData(AuthType.PHONE))
		AuthType.SMS -> router.navigateTo(AppScreens.AUTH_SCREEN_KEY, AuthData(AuthType.SMS))
		AuthType.CREATE_PASSWORD -> router.replaceScreen(AppScreens.AUTH_SCREEN_KEY, AuthData(AuthType.CREATE_PASSWORD))
		AuthType.PASSWORD -> router.replaceScreen(AppScreens.AUTH_SCREEN_KEY, AuthData(AuthType.PASSWORD))
	}

	private fun showNextStepOnButton(currentAuthType: AuthType) = when (currentAuthType) {
		AuthType.PHONE -> router.navigateTo(AppScreens.AUTH_SCREEN_KEY, AuthData(AuthType.SMS))
		AuthType.SMS -> router.navigateTo(AppScreens.AUTH_SCREEN_KEY, AuthData(AuthType.CREATE_PASSWORD))
		else -> Unit
	}

	private fun researchContentConsumer(action: AuthAction.ResearchContent) {
		if (action.content == lastContent) {
			return
		}

		lastContent = action.content

		authInteractor.validateData(action.content, authData.authType)
			.bindSubscribe(onSuccess = { validationResult(it, action.content) })
	}


	private fun onLoading() {
		viewState.updateValue {
			copy(screenState = PrimaryViewState.Loading, isResetContent = false)
		}
	}

	private fun onSuccess() {
		viewState.updateValue {
			copy(screenState = PrimaryViewState.Success, isResetContent = false)
		}
	}


	private fun validationResult(result: Result<Boolean>, content: CharSequence) = result
		.doIfSuccess { isValid ->
			if (!isValid) {
				viewState.updateValue {
					copy(screenState = PrimaryViewState.Data, isResetContent = false)
				}
				return@doIfSuccess
			}

			when (authData.authType) {
				AuthType.CREATE_PASSWORD,
				AuthType.PASSWORD -> viewState.updateValue { copy(screenState = PrimaryViewState.Success, isResetContent = false) }
				AuthType.SMS -> {
					onLoading()
					authInteractor
						.checkSms(content.toString())
						.bindSubscribe(onSuccess = {
							onSuccess()
							showNextStepByType(it.nextStep)
						})
				}
				AuthType.PHONE -> {
					onLoading()
					authInteractor
						.sendPhone(content.toString())
						.bindSubscribe(onSuccess = {
							onSuccess()
							showNextStepByType(it.nextStep)
						})
				}
			}
		}

	// MARK - Password Actions
	private fun onRememberPassword(password: String) {
		rememberedPassword = password
		viewState.updateValue {
			copy(isPassRepeated = true, isResetContent = true)
		}
	}

	private fun onRepeatPassword(password: String) {
		authInteractor.validateEquality(rememberedPassword, password)
			.bindSubscribe(onSuccess = { result ->
				result.doIfSuccess {
					rememberedPassword = EMPTY

					if (!it) {
						viewState.updateValue { copy(isPassRepeated = false, isResetContent = true) }
						return@doIfSuccess
					}

					createPassword(password)
				}
			})
	}

	private fun createPassword(password: String) {
		onLoading()
		authInteractor
			.createPassword(password)
			.bindSubscribe(onSuccess = {
				onSuccess()
				router.backTo(AppScreens.MAP_SCREEN_KEY)
			})
	}

	private fun checkPassword(password: String) {
		onLoading()
		authInteractor
			.checkPassword(password)
			.bindSubscribe(onSuccess = {
				onSuccess()
				router.backTo(AppScreens.MAP_SCREEN_KEY)
			})
	}


	// MARK - SMS Actions
	private fun startTimer() {
		timerDisposable = Observable.interval(0, 1, TimeUnit.SECONDS, Schedulers.computation())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe {
				val lostTime = atomicInteger.decrementAndGet()
				if (lostTime == 0) {
					stopTimer()
				}
				viewState.updateValue { copy(blockingSeconds = lostTime, isResetContent = false) }
			}
	}

	private fun onRefreshSms() {
		onLoading()
		authInteractor
			.refreshSms()
			.bindSubscribe(onSuccess = {
				onSuccess()
				startTimer()
				updatePhone()
			})
	}

	private fun updatePhone() {
		authInteractor.getPhone()
			.bindSubscribe(onSuccess = {
				viewState.updateValue { copy(authPhone = it, isResetContent = false) }
			})
	}

	private fun stopTimer() {
		atomicInteger.set(SECURE_RETRY_TIMEOUT)
		timerDisposable?.dispose()
	}

	// IMPLEMENT - ConnectionPlugin
	override fun onChangeConnection(connectionState: ConnectionState) {

	}

	override fun onCleared() {
		super.onCleared()
		timerDisposable?.dispose()
	}
}