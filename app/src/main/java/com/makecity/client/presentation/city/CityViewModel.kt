package com.makecity.client.presentation.city

import com.makecity.client.app.AppScreens
import com.makecity.client.data.geo.GeoDataSource
import com.makecity.client.data.geo.GeoPoint
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
import ru.terrakok.cicerone.Router


// State
@Presentation
data class CityViewState(
	override val screenState: PrimaryViewState = PrimaryViewState.Loading,
	val cities: List<Pair<Long, String>> = emptyList()
) : ViewState


// Action
sealed class CityAction : ActionView {
	object RefreshCities : CityAction()
	data class SelectCity(
		val cityId: Long
	) : CityAction()
}

// Reducer
interface CityReducer : StatementReducer<CityViewState, CityAction>


// ViewModel
class CityViewModel(
	private val router: Router,
	private val source: GeoDataSource,
	override val connectionProvider: ConnectionProvider,
	override val disposables: CompositeDisposable = CompositeDisposable()
) : BaseViewModel(), CityReducer, ReducerPluginConnection {

	override val viewState: StateLiveData<CityViewState> = StateLiveData.create(CityViewState())
	private var geoPoints: List<GeoPoint> = emptyList()

	init {
		prepareCities()
	}

	override fun reduce(action: CityAction) {
		when (action) {
			is CityAction.SelectCity -> {
				geoPoints
					.find { it.cityId == action.cityId }
					?.let {
						source.setDefaultCity(it)
							.bindSubscribe(onSuccess = {
								router.newRootScreen(AppScreens.MAP_SCREEN_KEY)
							})
					}
			}
			is CityAction.RefreshCities -> prepareCities()
		}
	}

	// IMPLEMENT - ConnectionPlugin
	override fun onChangeConnection(connectionState: ConnectionState) {}

	private fun prepareCities() {
		viewState.updateValue { copy(screenState = PrimaryViewState.Loading) }
		source.getActiveCities()
			.bindSubscribe(onSuccess = { list ->
				geoPoints = list
				viewState.updateValue {
					copy(
						screenState = PrimaryViewState.Data,
						cities = list.map { Pair(it.cityId, it.title) }
					)
				}
			})
	}
}