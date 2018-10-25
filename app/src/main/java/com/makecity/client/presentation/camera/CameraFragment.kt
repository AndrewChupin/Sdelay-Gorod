package com.makecity.client.presentation.camera

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.MotionEvent
import android.view.View
import com.makecity.client.R
import com.makecity.client.app.AppInjector
import com.makecity.client.presentation.lists.ImagesAdapter
import com.makecity.client.presentation.lists.ImagesListDelegate
import com.makecity.core.extenstion.calculateDiffs
import com.makecity.core.extenstion.withArguments
import com.makecity.core.presentation.screen.ToolbarScreen
import com.makecity.core.presentation.view.StatementFragment
import com.makecity.core.utils.image.ImageManager
import io.fotoapparat.Fotoapparat
import io.fotoapparat.configuration.CameraConfiguration
import io.fotoapparat.configuration.UpdateConfiguration
import io.fotoapparat.log.fileLogger
import io.fotoapparat.log.logcat
import io.fotoapparat.log.loggers
import io.fotoapparat.parameter.Flash
import io.fotoapparat.parameter.ScaleType
import io.fotoapparat.result.transformer.scaled
import io.fotoapparat.selector.autoFlash
import io.fotoapparat.selector.back
import io.fotoapparat.selector.off
import kotlinx.android.synthetic.main.fragment_camera.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject


typealias CameraStatement = StatementFragment<CameraReducer, CameraViewState, CameraAction>


class CameraFragment : CameraStatement(), ToolbarScreen, ImagesListDelegate {

	companion object {
		private const val ARGUMENT_CAMERA_DATA = "ARGUMENT_CAMERA_DATA"

		private const val PREVIEW_PHOTO_SIZE = 0.25f

		fun newInstance(data: CameraScreenData) = CameraFragment().withArguments {
			putParcelable(ARGUMENT_CAMERA_DATA, data)
		}
	}

	override val layoutId: Int = R.layout.fragment_camera

	lateinit var adapter: ImagesAdapter

	@Inject
	lateinit var imageManager: ImageManager
	private lateinit var camera: Fotoapparat

	override fun onInject() = AppInjector.inject(this, getArgument(ARGUMENT_CAMERA_DATA))

	override fun getToolbar(): Toolbar = toolbar

	override fun onViewCreatedBeforeRender(savedInstanceState: Bundle?) {
		camera = initCamera()

		adapter = ImagesAdapter(imageManager, this)
		camera_photos_list.layoutManager = LinearLayoutManager(requireContext())
		camera_photos_list.adapter = adapter

		make_photo_loop.setOnTouchListener(::cameraButtonMotionHandler)

		make_photo_loop.setOnClickListener {
			val photoResult = camera
				.takePicture()

			val file = imageManager.crateNewPictureFile()
			photoResult.saveToFile(file)

			photoResult
				.toBitmap(scaled(PREVIEW_PHOTO_SIZE))
				.whenAvailable { photo ->
					if (photo != null) {
						reducer.reduce(CameraAction.AddPhoto(Image(file.absolutePath)))
					}
				}
		}

		camera_image_back clickReduce CameraAction.CheckImageData
		camera_button_gallery clickReduce CameraAction.PickCameraPhoto
		camera_done clickReduce CameraAction.PhotosComplete

		camera_lightning_button.setOnClickListener { _ ->
			camera.getCurrentParameters().whenAvailable { params ->
				if (params != null) {
					val mode = if (params.flashMode == Flash.Off) {
						camera_lightning_button.setImageResource(R.drawable.ic_lightning_enable)
						autoFlash()
					} else  {
						camera_lightning_button.setImageResource(R.drawable.ic_lightning_disable)
						off()
					}
					camera.updateConfiguration(
						UpdateConfiguration(
							flashMode = mode
						)
					)
				}
			}
		}
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		reducer.reduce(CameraAction.CheckImageData)
	}

	override fun render(state: CameraViewState) {
		adapter.calculateDiffs(state.images)
	}

	override fun onStart() {
		super.onStart()
		camera.start()
	}

	override fun onStop() {
		super.onStop()
		camera.stop()
	}

	override fun onScreenResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onScreenResult(requestCode, resultCode, data)
		if (data != null) {
			reducer.reduce(CameraAction.AddPhoto(Image(data.dataString)))
		}
	}

	override fun onClickImage(image: Image) {

	}

	override fun onDeleteImage(image: Image) = reducer.reduce(CameraAction.DeletePhoto(image))


	private fun initCamera() = Fotoapparat(
		context = requireContext(),
		view = camera_view,
		scaleType = ScaleType.CenterCrop,
		lensPosition = back(),
		cameraConfiguration = CameraConfiguration(
			flashMode = autoFlash()
		),
		logger = loggers(
			logcat(),
			fileLogger(requireContext())
		),
		cameraErrorCallback = { error -> error.printStackTrace() }
	)

	private fun cameraButtonMotionHandler(
		view : View,
		event: MotionEvent
	): Boolean = when (event.action) {
		MotionEvent.ACTION_CANCEL,
		MotionEvent.ACTION_UP -> {
			view.animate()
				.scaleX(0.8f)
				.scaleY(0.8f)
				.setDuration(300)
				.start()
			false
		}
		MotionEvent.ACTION_DOWN -> {
			camera.focus()
			view.animate()
				.scaleX(1.2f)
				.scaleY(1.2f)
				.setDuration(300)
				.start()
			false
		}
		else -> false
	}
}