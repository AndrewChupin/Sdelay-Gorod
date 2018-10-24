package com.makecity.client.presentation.map

import com.makecity.client.app.AppScreens
import com.makecity.client.data.task.Task
import com.makecity.client.domain.map.TaskPointsInteractor
import com.makecity.client.presentation.category.CategoryData
import com.makecity.client.presentation.category.CategoryType
import com.makecity.client.presentation.problem.ProblemData
import com.makecity.core.data.Presentation
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
import ru.terrakok.cicerone.Router


// State
@Presentation
data class MapPointsViewState(
	override val screenState: PrimaryViewState = PrimaryViewState.Loading,
	val tasks: List<Task> = emptyList(),
	override val locationState: LocationState = LocationState.Unknown,
	override val connectionState: ConnectionState = ConnectionState.Unknown
) : ViewState, ViewStatePluginConnection, ViewStatePluginLocation


// Action
sealed class MapPointsAction: ActionView {
	object ShowCamera : MapPointsAction()
	object LoadMapPoints : MapPointsAction()
	object ShowProblemsAsList : MapPointsAction()
	object ShowMenu : MapPointsAction()
	data class ShowDetails(
		val problemId: Long
	): MapPointsAction()
}


// Reducer
interface MapPointsReducer: StatementReducer<MapPointsViewState, MapPointsAction>


// ViewModel
class MapPointsViewModel(
	private val router: Router,
	private val mapPointsInteractor: TaskPointsInteractor,
	override val connectionProvider: ConnectionProvider,
	override val permissionManager: PermissionManager,
	override val locationProvider: LocationProvider,
	override val locationPluginConfig: LocationPluginConfig = LocationPluginConfig(),
	override val disposables: CompositeDisposable = CompositeDisposable()
) : BaseViewModel(), MapPointsReducer, ReducerPluginConnection, ReducerPluginLocation {

	override val viewState: StateLiveData<MapPointsViewState> = StateLiveData.create(MapPointsViewState())

	init {
		startObserveLocation()
		checkConnection()
		startObserveConnection()
	}


	// OVERRIDE - Reducer
	override fun reduce(action: MapPointsAction) {
		when (action) {
			is MapPointsAction.LoadMapPoints -> mapPointsInteractor
				.loadProblems()
				.bindSubscribe(
					onSuccess = ::reduceLoadTasksSuccess,
					onError = Throwable::printStackTrace
				)
			is MapPointsAction.ShowProblemsAsList -> router.navigateTo(AppScreens.FEED_SCREEN_KEY)
			is MapPointsAction.ShowMenu -> router.navigateTo(AppScreens.MENU_SCREEN_KEY)
			is MapPointsAction.ShowDetails -> router.navigateTo(AppScreens.PROBLEM_SCREEN_KEY, ProblemData(action.problemId))
			is MapPointsAction.ShowCamera ->
				router.navigateTo(AppScreens.CAMERA_SCREEN_KEY)
		}
	}


	// OVERRIDE - ConnectionPlugin
	override fun onChangeConnection(connectionState: ConnectionState) {
		viewState.updateValue {
			copy(connectionState = connectionState, locationState = LocationState.Unknown)
		}
	}


	// OVERRIDE - LocationPlugin
	override fun onLocationPermissionStateChanged(permissionState: PermissionState) {

	}

	override fun onLocationChanged(locationState: LocationState)  = when (locationState) {
		is LocationState.Founded -> viewState.updateValue {
			state.copy(locationState = locationState)
		}
		else -> Unit
	}


	// IMPLEMENT - ReduceFunctions
	private fun reduceLoadTasksSuccess(tasks: List<Task>) {
		viewState.updateValue {
			copy(
				screenState = PrimaryViewState.Data,
				tasks = tasks,
				locationState = LocationState.Unknown
			)
		}
	}
}