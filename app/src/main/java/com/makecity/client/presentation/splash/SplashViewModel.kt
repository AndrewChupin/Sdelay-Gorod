package com.makecity.client.presentation.splash

import com.makecity.client.app.AppScreens
import com.makecity.client.data.geo.GeoDataSource
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
sealed class SplashAction: ActionView {
	object PrepareData: SplashAction()
}


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
	private val geoDataSource: GeoDataSource,
	private val resourceManager: ResourceManager,
	override val connectionProvider: ConnectionProvider,
	override val disposables: CompositeDisposable = CompositeDisposable()
) : ReactiveViewModel(), SplashReducer, ReducerPluginConnection {

	override val viewState = StateLiveData.create(SplashDataViewState())

	init {
		checkConnection()
		startObserveConnection()
	}

	override fun reduce(action: SplashAction) {
		when(action) {
			is SplashAction.PrepareData -> geoDataSource.refreshCity()
				.bindSubscribe(onSuccess = {
					router.replaceScreen(AppScreens.MAP_SCREEN_KEY)
				}, onError = {
					if (state.connectionState == ConnectionState.Connect) {
						router.navigateTo(AppScreens.CITY_SCREEN_KEY)
					}
				})
		}
	}

	override fun onChangeConnection(connectionState: ConnectionState) {
		viewState.value = state.copy(connectionState = connectionState)
	}
}
