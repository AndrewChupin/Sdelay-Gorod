package com.makecity.core.presentation.view

import android.content.Context
import android.os.Bundle
import android.support.annotation.CallSuper
import android.view.View
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.makecity.core.presentation.viewmodel.ActionView
import com.makecity.core.presentation.viewmodel.BaseReducer
import com.makecity.core.presentation.viewmodel.StatementReducer
import com.makecity.core.presentation.state.ViewState
import javax.inject.Inject


abstract class BaseMapFragment: BaseFragment(), OnMapReadyCallback {

	private lateinit var mapViews: List<MapView>
	lateinit var googleMap: GoogleMap

	abstract fun setupMapView(): List<MapView>

	fun addMapView(mapView: MapView) {
		mapViews = mapViews.plus(mapView)
	}

	@CallSuper
	override fun onViewCreated(view: android.view.View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		mapViews = setupMapView()
		mapViews.forEach {
			it.onCreate(savedInstanceState)
		}
	}

	override fun onMapReady(map: GoogleMap) {
		googleMap = map
	}

	@CallSuper
	override fun onStart() {
		super.onStart()
		mapViews.forEach {
			it.onStart()
		}
	}

	@CallSuper
	override fun onResume() {
		super.onResume()
		mapViews.forEach {
			it.onResume()
		}
	}

	@CallSuper
	override fun onPause() {
		mapViews.forEach {
			it.onPause()
		}
		super.onPause()
	}

	@CallSuper
	override fun onStop() {
		mapViews.forEach {
			it.onStop()
		}
		super.onStop()
	}

	@CallSuper
	override fun onDestroyView() {
		mapViews.forEach {
			it.onDestroy()
		}
		super.onDestroyView()
	}

	@CallSuper
	override fun onLowMemory() {
		mapViews.forEach {
			it.onLowMemory()
		}
		super.onLowMemory()
	}
}


abstract class ReducibleViewMapFragment<Reducer: BaseReducer<AG>, AG: ActionView>: BaseMapFragment(), ReducibleView<Reducer, AG>, InjectableView {

	@Inject
	override lateinit var reducer: Reducer

	override fun onAttach(context: Context?) {
		onInject()
		super.onAttach(context)
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
