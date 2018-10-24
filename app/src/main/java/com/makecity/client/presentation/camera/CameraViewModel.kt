package com.makecity.client.presentation.camera

import android.Manifest
import android.util.Log
import com.makecity.client.app.AppScreens
import com.makecity.client.presentation.category.CategoryData
import com.makecity.client.presentation.category.CategoryType
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
import com.makecity.core.utils.permission.PermissionManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import ru.terrakok.cicerone.Router

// State
@Presentation
data class CameraViewState(
	override val screenState: PrimaryViewState = PrimaryViewState.Loading
) : ViewState


// Action
sealed class CameraAction: ActionView {
	object ShowCategory: CameraAction()
}


// Reducer
interface CameraReducer: StatementReducer<CameraViewState, CameraAction>


// ViewModel
class CameraViewModel(
	private val router: Router,
	override val connectionProvider: ConnectionProvider,
	private val permissionManager: PermissionManager,
	override val disposables: CompositeDisposable = CompositeDisposable()
) : BaseViewModel(), CameraReducer, ReducerPluginConnection {

	override val viewState: StateLiveData<CameraViewState> = StateLiveData.create(CameraViewState())

	override fun reduceAfterReady() {
		permissionManager.requestPermission(
			Manifest.permission.READ_EXTERNAL_STORAGE,
			Manifest.permission.WRITE_EXTERNAL_STORAGE,
			Manifest.permission.CAMERA
		).bindSubscribe(
				scheduler = AndroidSchedulers.mainThread(),
				onNext = {
					when {
						it.granted -> {

						}
						it.shouldShowRequestPermissionRationale -> {
							Log.d("Logod", "shouldShowRequestPermissionRationale")
						}
						else -> {
							Log.d("Logod", "danie")
						}
					}
				},
				onError = {
					it.printStackTrace()
				})
	}

	override fun reduce(action: CameraAction) {
		if (action is CameraAction.ShowCategory) {
			router.navigateTo(AppScreens.CATEGORY_SCREEN_KEY, CategoryData(CategoryType.CATEGORY))
		}
	}

	// IMPLEMENT - ConnectionPlugin
	override fun onChangeConnection(connectionState: ConnectionState) {

	}
}