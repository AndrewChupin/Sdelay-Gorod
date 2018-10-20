package com.makecity.client.presentation.map

import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView.HORIZONTAL
import android.view.View
import com.makecity.client.R
import com.makecity.client.app.AppInjector
import com.makecity.core.extenstion.calculateDiffs
import com.makecity.core.extenstion.hideWithScale
import com.makecity.core.extenstion.showWithScale
import com.makecity.core.plugin.connection.ConnectionState
import com.makecity.core.plugin.location.LocationState
import com.makecity.core.presentation.state.PrimaryViewState
import com.makecity.core.presentation.view.MapStatementFragment
import kotlinx.android.synthetic.main.bottom_sheet_problems.*
import kotlinx.android.synthetic.main.fragment_map.*
import android.support.v7.widget.LinearSnapHelper
import com.makecity.client.presentation.feed.ShowProblemDetails
import com.makecity.core.data.entity.Location
import com.makecity.core.presentation.list.snap.OnSnapPositionChangeListener
import com.makecity.core.presentation.list.snap.SnapOnScrollListener
import com.makecity.core.presentation.view.map.BaseMapView


typealias MapStatement = MapStatementFragment<MapPointsReducer, MapPointsViewState, MapPointsAction>


class MapPointsFragment : MapStatement(), OnSnapPositionChangeListener {

	companion object {
		fun newInstance() = MapPointsFragment()
	}

	override val layoutId: Int = R.layout.fragment_map
	private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
	private lateinit var problemsMapAdapter: ProblemsMapAdapter

	override fun onInject() {
		AppInjector.inject(this)
	}

	override fun setupMapView(): BaseMapView = map_view

	override fun onViewCreatedBeforeRender(savedInstanceState: Bundle?) {
		bottomSheetBehavior = initBottomState(bottom_problems)


		problemsMapAdapter = ProblemsMapAdapter {
			reducer.reduce(ShowDetails(it.id))
		}
		bottom_problems_list.layoutManager = LinearLayoutManager(requireContext(), HORIZONTAL, false)
		bottom_problems_list.adapter = problemsMapAdapter

		val snapHelper = LinearSnapHelper()
		val snapOnScrollListener = SnapOnScrollListener(snapHelper, this,
			SnapOnScrollListener.Behavior.NOTIFY_ON_SCROLL_STATE_IDLE)

		snapHelper.attachToRecyclerView(bottom_problems_list)
		bottom_problems_list.addOnScrollListener(snapOnScrollListener)

		bottom_hide_button.setOnClickListener {
			bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
		}

		map_view.pointClickListener = {
			reducer.state.tasks
				.forEachIndexed { index, task ->
					if (task.id == it.id) {
						bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
						bottom_problems_list.smoothScrollToPosition(index)
					}
				}
			true
		}

		map_show_as_list.setOnClickListener {
			reducer.reduce(ShowProblemsAsList)
		}

		map_menu_button.setOnClickListener {
			reducer.reduce(ShowMenu)
		}
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		map_view.mapInteractionReady = {
			reducer.reduce(LoadMapPoints)
		}
	}

	override fun render(state: MapPointsViewState) {
		when (state.connectionState) {
			is ConnectionState.Connect -> text_map_connection.hideWithScale()
			is ConnectionState.Disconnect -> text_map_connection.showWithScale()
		}

		when (state.locationState) {
			is LocationState.Founded -> map_view.setCamera(state.locationState.location)
		}

		when (state.screenState) {
			is PrimaryViewState.Data -> {
				map_view.updatePoints(state.tasks)
				map_group_message.hideWithScale()
				problemsMapAdapter.calculateDiffs(state.tasks)
			}
			is PrimaryViewState.Loading -> map_group_message.showWithScale()
		}
	}

	override fun onSnapPositionChange(position: Int) {
		val task = reducer.state.tasks[position]
		map_view.setCamera(Location(task.latitude, task.longitude), withAnimation = true)
	}

	private fun initBottomState(view: View): BottomSheetBehavior<View> {
		val bottomSheetBehavior = BottomSheetBehavior.from(view)
		bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
		bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
			override fun onSlide(p0: View, p1: Float) {}

			override fun onStateChanged(p0: View, state: Int) {
				when (state) {
					BottomSheetBehavior.STATE_HIDDEN -> map_fab_add_task.show()
					BottomSheetBehavior.STATE_EXPANDED -> map_fab_add_task.hide()
				}
			}

		})
		return bottomSheetBehavior
	}
}
