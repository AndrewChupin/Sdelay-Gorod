package com.makecity.client.presentation.map_address

import com.makecity.client.app.AppScreens
import com.makecity.client.data.address.Address
import com.makecity.core.data.Presentation
import com.makecity.core.data.entity.Location
import com.makecity.core.plugin.connection.ConnectionProvider
import com.makecity.core.plugin.connection.ConnectionState
import com.makecity.core.plugin.connection.ReducerPluginConnection
import com.makecity.core.plugin.connection.ViewStatePluginConnection
import com.makecity.core.plugin.location.*
import com.makecity.core.presentation.state.PrimaryViewState
import com.makecity.core.presentation.state.StateLiveData
import com.makecity.core.presentation.state.ViewState
import com.makecity.core.presentation.viewmodel.ActionView
import com.makecity.core.presentation.viewmodel.BaseViewModel
import com.makecity.core.presentation.viewmodel.StatementReducer
import com.makecity.core.utils.permission.PermissionManager
import com.makecity.core.utils.permission.PermissionState
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import ru.terrakok.cicerone.Router
import java.util.concurrent.TimeUnit


// State
@Presentation
data class MapAddressViewState(
	override val screenState: PrimaryViewState = PrimaryViewState.Loading,
	override val locationState: LocationState = LocationState.Unknown,
	override val connectionState: ConnectionState = ConnectionState.Unknown,
	val address: Address? = null
) : ViewState, ViewStatePluginConnection, ViewStatePluginLocation


// Action
sealed class MapAddressAction: ActionView {
	object FindOwnLocation: MapAddressAction()
	data class GetPoints(
		val locationCenter: Location
	): MapAddressAction()
	object ShowProblemPreview: MapAddressAction()
}


// Reducer
interface MapAddressReducer: StatementReducer<MapAddressViewState, MapAddressAction>


// ViewModel
class MapAddressViewModel(
	private val router: Router,
	override val connectionProvider: ConnectionProvider,
	override val permissionManager: PermissionManager,
	override val locationProvider: LocationProvider,
	override val locationPluginConfig: LocationPluginConfig = LocationPluginConfig(),
	override val disposables: CompositeDisposable = CompositeDisposable()
) : BaseViewModel(), MapAddressReducer, ReducerPluginConnection, ReducerPluginLocation {

	override val viewState: StateLiveData<MapAddressViewState> = StateLiveData.create(MapAddressViewState())

	init {
		startObserveLocation()
		checkConnection()
		startObserveConnection()
	}

	override fun reduce(action: MapAddressAction) {
		when (action) {
			is MapAddressAction.FindOwnLocation -> startObserveLocation()
			is MapAddressAction.GetPoints -> {
				viewState.updateValue {
					copy(locationState = LocationState.Unknown, address = null, screenState = PrimaryViewState.Loading)
				}
				Single.just(Address("ул. Ленина, 86"))
					.delay(2, TimeUnit.SECONDS)
					.bindSubscribe(onSuccess = {
						viewState.updateValue {
							copy(locationState = LocationState.Unknown, address = it, screenState = PrimaryViewState.Data)
						}
					})
			}
			is MapAddressAction.ShowProblemPreview -> router.navigateTo(AppScreens.CREATE_PROBLEM_SCREEN_KEY)
		}
	}

	// IMPLEMENT - ConnectionPlugin
	override fun onChangeConnection(connectionState: ConnectionState) {
		viewState.updateValue {
			copy(connectionState = connectionState, locationState = LocationState.Unknown)
		}
	}

	override fun onLocationPermissionStateChanged(permissionState: PermissionState) = when (permissionState) {
		is PermissionState.Failure -> router.showSystemMessage("Permission denied")
		else -> Unit
	}

	override fun onLocationChanged(locationState: LocationState) = when (locationState) {
		is LocationState.Founded -> viewState.value = state.copy(locationState = locationState)
		else -> Unit
	}
}