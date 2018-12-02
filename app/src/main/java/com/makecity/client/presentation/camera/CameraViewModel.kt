package com.makecity.client.presentation.camera

import android.Manifest
import android.os.Parcelable
import android.util.Log
import com.makecity.client.app.AppScreens
import com.makecity.client.data.temp_problem.TempProblemDataSource
import com.makecity.client.presentation.category.CategoryScreenData
import com.makecity.client.presentation.category.CategoryType
import com.makecity.client.presentation.create_problem.ProblemCreatingType
import com.makecity.core.data.Presentation
import com.makecity.core.extenstion.blockingCompletable
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
import kotlinx.android.parcel.Parcelize
import ru.terrakok.cicerone.Router


// Data
@Parcelize
data class CameraScreenData(
	val problemCreatingType: ProblemCreatingType
) : Parcelable


data class Image(
	val path: String
)


// State
@Presentation
data class CameraViewState(
	override val screenState: PrimaryViewState = PrimaryViewState.Data,
	val images: List<Image> = emptyList()
) : ViewState


// Action
sealed class CameraAction : ActionView {
	object PickCameraPhoto : CameraAction()
	object PhotosComplete : CameraAction()
	object CheckImageData : CameraAction()
	object Exit : CameraAction()

	data class DeletePhoto(
		val image: Image
	) : CameraAction()

	data class AddPhoto(
		val image: Image
	) : CameraAction()
}


// Reducer
interface CameraReducer : StatementReducer<CameraViewState, CameraAction>


// ViewModel
class CameraViewModel(
	private val router: Router,
	private val data: CameraScreenData,
	private val tempProblemDataSource: TempProblemDataSource,
	override val connectionProvider: ConnectionProvider,
	private val permissionManager: PermissionManager,
	override val disposables: CompositeDisposable = CompositeDisposable()
) : BaseViewModel(), CameraReducer, ReducerPluginConnection {

	companion object {
		private const val MAX_COUNT_IMAGES = 2
	}

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
						Log.d("Logod", "granted ${it.name}")
					}
					it.shouldShowRequestPermissionRationale -> {
						// TODO LATE
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
		when (action) {
			is CameraAction.Exit -> router.exit()

			is CameraAction.PickCameraPhoto -> {
				permissionManager.requestPermission(
					Manifest.permission.READ_EXTERNAL_STORAGE
				).bindSubscribe(
					scheduler = AndroidSchedulers.mainThread(),
					onNext = {
						when {
							it.granted -> router.navigateTo(AppScreens.IMAGE_PICKER_SCREEN_KEY)
						}
					},
					onError = {
						it.printStackTrace()
					})
			}

			is CameraAction.DeletePhoto -> tempProblemDataSource
				.getTempProblem()
				.map { problem ->
					problem.copy(images = problem.images.filterNot { it == action.image.path })
				}
				.blockingCompletable(tempProblemDataSource::saveTempProblem)
				.bindSubscribe(onSuccess = {
					viewState.updateValue {
						copy(images = it.images.map(::Image))
					}
				})

			is CameraAction.PhotosComplete -> navigateComplete()

			is CameraAction.CheckImageData -> tempProblemDataSource
				.getTempProblem()
				.bindSubscribe(onSuccess = {
					viewState.updateValue {
						copy(images = it.images.map(::Image))
					}
				})

			is CameraAction.AddPhoto -> tempProblemDataSource
				.getTempProblem()
				.map {
					val images = clipListToMaxSize(it.images)
					it.copy(images = images.plus(action.image.path))
				}
				.flatMapCompletable(tempProblemDataSource::saveTempProblem)
				.bindSubscribe(onSuccess = {
					viewState.updateValue {
						val newImages = clipListToMaxSize(images)
						copy(images = newImages.plus(action.image))
					}
				})
		}
	}

	private fun navigateComplete() {
		if (data.problemCreatingType == ProblemCreatingType.NEW) {
			router.navigateTo(
				AppScreens.CATEGORY_SCREEN_KEY,
				CategoryScreenData(CategoryType.CATEGORY, ProblemCreatingType.NEW)
			)
		} else {
			router.exit()
		}
	}

	// IMPLEMENT - ConnectionPlugin
	override fun onChangeConnection(connectionState: ConnectionState) {

	}

	private fun <T> clipListToMaxSize(list: List<T>): List<T> =
		if (list.size >= MAX_COUNT_IMAGES) list.drop(1) else list
}