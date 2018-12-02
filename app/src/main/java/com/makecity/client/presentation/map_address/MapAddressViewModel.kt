package com.makecity.client.presentation.map_address

import android.os.Parcelable
import com.makecity.client.app.AppScreens
import com.makecity.client.data.address.Address
import com.makecity.client.data.address.AddressDataSource
import com.makecity.client.data.temp_problem.TempProblemDataSource
import com.makecity.client.presentation.create_problem.CreateProblemData
import com.makecity.client.presentation.create_problem.ProblemCreatingType
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
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.parcel.Parcelize
import ru.terrakok.cicerone.Router


// Data
@Parcelize
data class MapAddressScreenData(
	val problemCreatingType: ProblemCreatingType
): Parcelable



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
	private val data: MapAddressScreenData,
	private val problemDataSource: TempProblemDataSource,
	private val addressDataSource: AddressDataSource,
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
		startObserveAddress()
	}

	override fun reduce(action: MapAddressAction) {
		when (action) {
			is MapAddressAction.FindOwnLocation -> startObserveLocation()
			is MapAddressAction.GetPoints -> {
				viewState.updateValue {
					copy(locationState = LocationState.Unknown, address = null, screenState = PrimaryViewState.Loading)
				}
				addressDataSource.getAddress(action.locationCenter)
			}
			is MapAddressAction.ShowProblemPreview -> saveAddress(
				state.address
					?: throw IllegalStateException("ShowProblemPreview with null address immposible")
			)
		}
	}

	private fun saveAddress(address: Address) {
		problemDataSource.getTempProblem()
			.map { it.copy(
				latitude = address.location.latitude,
				longitude = address.location.longitude,
				address = "${address.street}, ${address.homeNumber}")
			}
			.flatMapCompletable(problemDataSource::saveTempProblem)
			.bindSubscribe(onSuccess = ::navigateComplete)
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

	private fun navigateComplete() {
		if (data.problemCreatingType == ProblemCreatingType.NEW) {
			router.backTo(AppScreens.MAP_SCREEN_KEY)
			router.navigateTo(AppScreens.CREATE_PROBLEM_SCREEN_KEY, CreateProblemData(canEdit = true))
		} else {
			router.exit()
		}
	}

	private fun startObserveAddress() {
		addressDataSource
			.observeAddress()
			.bindSubscribe (onNext = {
			viewState.updateValue {
				copy(locationState = LocationState.Unknown, address = it, screenState = PrimaryViewState.Data)
			}
		})
	}
}