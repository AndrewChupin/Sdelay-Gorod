package com.makecity.client.presentation.splash

import com.makecity.client.app.AppScreens
import com.makecity.core.plugin.connection.ConnectionProvider
import com.makecity.core.plugin.connection.ConnectionState
import com.makecity.core.plugin.connection.ReducerPluginConnection
import com.makecity.core.plugin.connection.ViewStatePluginConnection
import com.makecity.core.presentation.state.PrimaryViewState
import com.makecity.core.presentation.state.StateLiveData
import com.makecity.core.presentation.state.ViewState
import com.makecity.core.presentation.viewmodel.ActionView
import com.makecity.core.presentation.viewmodel.ReactiveViewModel
import com.makecity.core.presentation.viewmodel.StatementReducer
import com.makecity.core.utils.resources.ResourceManager
import io.reactivex.disposables.CompositeDisposable
import ru.terrakok.cicerone.Router
import javax.inject.Inject


// Actions
sealed class SplashAction: ActionView
object ShowMapAction: SplashAction()
object PreparePartnerAction: SplashAction()


// State
data class SplashDataViewState(
	override val screenState: PrimaryViewState = PrimaryViewState.Loading,
	override val connectionState: ConnectionState = ConnectionState.Unknown
): ViewState, ViewStatePluginConnection


// Reducer
interface SplashReducer: StatementReducer<SplashDataViewState, SplashAction>


// View Model
class SplashViewModel @Inject constructor(
	private val router: Router,
	private val resourceManager: ResourceManager,
	override val connectionProvider: ConnectionProvider,
	override val disposables: CompositeDisposable = CompositeDisposable()
) : ReactiveViewModel(), SplashReducer, ReducerPluginConnection {

	override val viewState = StateLiveData.create(SplashDataViewState())

	init {
		checkConnection()
		startObserveConnection()
	}

	override fun reduce(action: SplashAction) = when(action) {
		is ShowMapAction -> showMap()
		is PreparePartnerAction -> {} // TODO
	}

	override fun onChangeConnection(connectionState: ConnectionState) {
		viewState.value = state.copy(connectionState = connectionState)
	}

	private fun showMap() = router.replaceScreen(AppScreens.MAP_SCREEN_KEY)

}
