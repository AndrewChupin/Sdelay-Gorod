package com.makecity.client.presentation.map

import com.makecity.client.app.AppScreens
import com.makecity.client.data.auth.AuthDataSource
import com.makecity.client.data.auth.AuthState
import com.makecity.client.data.auth.AuthType
import com.makecity.client.data.auth.TokenNotFounded
import com.makecity.client.data.profile.ProfileDataSource
import com.makecity.client.data.task.Task
import com.makecity.client.data.temp_problem.TempProblemDataSource
import com.makecity.client.domain.map.TaskPointsInteractor
import com.makecity.client.presentation.auth.AuthData
import com.makecity.client.presentation.camera.CameraScreenData
import com.makecity.client.presentation.create_problem.ProblemCreatingType
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
import com.makecity.core.utils.resources.ResourceManager
import io.reactivex.disposables.CompositeDisposable
import ru.terrakok.cicerone.Router


// State
@Presentation
data class MapPointsViewState(
	override val screenState: PrimaryViewState = PrimaryViewState.Loading,
	val tasks: List<Task> = emptyList(),
	val authState: AuthState = AuthState.UNDEFINED,
	override val locationState: LocationState = LocationState.Unknown,
	override val connectionState: ConnectionState = ConnectionState.Unknown
) : ViewState, ViewStatePluginConnection, ViewStatePluginLocation


// Action
sealed class MapPointsAction: ActionView {
	object CreateTask : MapPointsAction()
	object LoadMapPoints : MapPointsAction()
	object ShowProblemsAsList : MapPointsAction()
	object ShowMenu : MapPointsAction()
	object ShowAuth : MapPointsAction()

	data class ShowDetails(
		val problemId: Long
	): MapPointsAction()
}


// Reducer
interface MapPointsReducer: StatementReducer<MapPointsViewState, MapPointsAction>


// ViewModel
class MapPointsViewModel(
	private val router: Router,
	private val tempProblemDataSource: TempProblemDataSource,
	private val authDataSource: AuthDataSource,
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
			is MapPointsAction.ShowProblemsAsList -> router.navigateTo(AppScreens.FEED_SCREEN_KEY)
			is MapPointsAction.ShowMenu -> router.navigateTo(AppScreens.MENU_SCREEN_KEY)
			is MapPointsAction.ShowAuth -> router.navigateTo(AppScreens.AUTH_SCREEN_KEY, AuthData(AuthType.PHONE))
			is MapPointsAction.ShowDetails -> router.navigateTo(AppScreens.PROBLEM_SCREEN_KEY, ProblemData(action.problemId))

			is MapPointsAction.LoadMapPoints -> {
				mapPointsInteractor
					.loadProblems()
					.bindSubscribe(
						onSuccess = ::reduceLoadTasksSuccess,
						onError = Throwable::printStackTrace
					)

				authDataSource
					.getToken()
					.bindSubscribe(onSuccess = {
						viewState.updateValue { copy(authState = AuthState.AUTH) }
					}, onError = {
						if (it is TokenNotFounded) {
							viewState.updateValue { copy(authState = AuthState.NOT_AUTH) }
						}
					})
			}

			is MapPointsAction.CreateTask -> tempProblemDataSource
				.isProblemExist()
				.bindSubscribe(onSuccess = { isExist ->
					if (isExist) {
						router.navigateTo(AppScreens.RESTORE_SCREEN_KEY)
					} else {
						router.navigateTo(AppScreens.CAMERA_SCREEN_KEY, CameraScreenData(ProblemCreatingType.NEW))
					}
				})
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