package com.makecity.core.presentation.view

import android.content.Context
import android.os.Bundle
import android.support.annotation.CallSuper
import android.view.View
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.makecity.core.presentation.state.ViewState
import com.makecity.core.presentation.view.map.BaseMapView
import com.makecity.core.presentation.viewmodel.ActionView
import com.makecity.core.presentation.viewmodel.BaseReducer
import com.makecity.core.presentation.viewmodel.StatementReducer
import javax.inject.Inject


abstract class BaseMapFragment: BaseFragment(), OnMapReadyCallback {

	private lateinit var mapView: BaseMapView
	lateinit var googleMap: GoogleMap
	protected var savedMapState: Bundle? = null

	abstract fun setupMapView(): BaseMapView

	@CallSuper
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		mapView = setupMapView()

		mapView.onCreate(savedInstanceState)
		mapView.restoreCameraState(savedMapState)
		savedMapState = null
	}

	override fun onMapReady(map: GoogleMap) {
		googleMap = map
	}

	@CallSuper
	override fun onResume() {
		super.onResume()
		mapView.onResume()
	}

	@CallSuper
	override fun onPause() {
		mapView.onPause()
		super.onPause()
	}

	@CallSuper
	override fun onDestroy() {
		mapView.onDestroy()
		super.onDestroy()
	}

	@CallSuper
	override fun onDestroyView() {
		super.onDestroyView()
		savedMapState = Bundle()
		mapView.storeCameraState(savedMapState)
	}

	@CallSuper
	override fun onLowMemory() {
		mapView.onLowMemory()
		super.onLowMemory()
	}

	@CallSuper
	override fun onSaveInstanceState(outState: Bundle) {
		mapView.onSaveInstanceState(outState)
		super.onSaveInstanceState(outState)
	}
}


abstract class ReducibleViewMapFragment<Reducer: BaseReducer<AG>, AG: ActionView>: BaseMapFragment(), ReducibleView<Reducer, AG>, InjectableView {

	@Inject
	override lateinit var reducer: Reducer

	override fun onAttach(context: Context?) {
		onInject()
		super.onAttach(context)
	}

	infix fun View?.clickReduce(action: AG) {
		this?.setOnClickListener {
			reducer.reduce(action)
		}
	}
}

abstract class MapStatementFragment<Reducer: StatementReducer<State, AG>, State: ViewState, AG: ActionView>
	: ReducibleViewMapFragment<Reducer, AG>(), RenderableView<State> {

	private var isInstanceSaved: Boolean = false

	abstract fun onViewCreatedBeforeRender(savedInstanceState: Bundle?)

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		onViewCreatedBeforeRender(savedInstanceState)
		reducer.viewState.observeThis {
			if (!isInstanceSaved) render(it)
		}
	}

	@CallSuper
	override fun onResume() {
		super.onResume()
		isInstanceSaved = false
	}

	@CallSuper
	override fun onSaveInstanceState(outState: Bundle) {
		super.onSaveInstanceState(outState)
		isInstanceSaved = true
	}

	override fun onDestroyView() {
		reducer.viewState.removeObservers(this)
		super.onDestroyView()
	}
}
