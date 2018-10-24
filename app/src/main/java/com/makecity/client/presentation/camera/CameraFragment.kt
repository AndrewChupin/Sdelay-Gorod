package com.makecity.client.presentation.camera

import android.os.Bundle
import android.support.v7.widget.Toolbar
import com.makecity.client.R
import com.makecity.client.app.AppInjector
import com.makecity.core.presentation.screen.ToolbarScreen
import com.makecity.core.presentation.view.StatementFragment
import io.fotoapparat.Fotoapparat
import io.fotoapparat.configuration.CameraConfiguration
import io.fotoapparat.log.fileLogger
import io.fotoapparat.log.logcat
import io.fotoapparat.log.loggers
import io.fotoapparat.parameter.ScaleType
import io.fotoapparat.selector.back
import kotlinx.android.synthetic.main.fragment_camera.*
import kotlinx.android.synthetic.main.toolbar.*


typealias CameraStatement = StatementFragment<CameraReducer, CameraViewState, CameraAction>


class CameraFragment : CameraStatement(), ToolbarScreen {

	companion object {
		fun newInstance() = CameraFragment()
	}

	override val layoutId: Int = R.layout.fragment_camera

	private lateinit var foto: Fotoapparat

	override fun onInject() = AppInjector.inject(this)

	override fun getToolbar(): Toolbar = toolbar

	override fun onViewCreatedBeforeRender(savedInstanceState: Bundle?) {
		foto = Fotoapparat(
			context = requireContext(),
			view = camera_view,
			scaleType = ScaleType.CenterCrop,
			lensPosition = back(),
			cameraConfiguration = CameraConfiguration(), // (optional) define an advanced configuration
			logger = loggers(                    // (optional) we want to log camera events in 2 places at once
				logcat(),                   // ... in logcat
				fileLogger(requireContext())            // ... and to file
			),
			cameraErrorCallback = { error -> }   // (optional) log fatal errors
		)

		make_photo_button.setOnClickListener {
			val photoResult = foto.takePicture()
		}

		camera_done.setOnClickListener {
			reducer.reduce(CameraAction.ShowCategory)
		}
	}

	override fun render(state: CameraViewState) {

	}

	override fun onStart() {
		super.onStart()
		foto.start()
	}

	override fun onStop() {
		super.onStop()
		foto.stop()
	}
}