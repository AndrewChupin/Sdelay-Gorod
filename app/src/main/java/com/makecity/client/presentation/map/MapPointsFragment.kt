package com.makecity.client.presentation.map

import android.animation.ObjectAnimator
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSnapHelper
import android.support.v7.widget.RecyclerView.HORIZONTAL
import android.view.MotionEvent
import android.view.View
import android.widget.SeekBar
import com.makecity.client.R
import com.makecity.client.app.AppInjector
import com.makecity.client.data.auth.AuthState
import com.makecity.core.data.entity.Location
import com.makecity.core.extenstion.calculateDiffs
import com.makecity.core.extenstion.hideWithScale
import com.makecity.core.extenstion.isVisible
import com.makecity.core.extenstion.showWithScale
import com.makecity.core.plugin.connection.ConnectionState
import com.makecity.core.plugin.location.LocationState
import com.makecity.core.presentation.list.snap.OnSnapPositionChangeListener
import com.makecity.core.presentation.list.snap.SnapOnScrollListener
import com.makecity.core.presentation.state.PrimaryViewState
import com.makecity.core.presentation.view.MapStatementFragment
import com.makecity.core.presentation.view.map.BaseMapView
import com.makecity.core.utils.ScreenUtils
import kotlinx.android.synthetic.main.bottom_sheet_problems.*
import kotlinx.android.synthetic.main.fragment_map.*


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
			reducer.reduce(MapPointsAction.ShowDetails(it.id))
		}
		bottom_problems_list.layoutManager = LinearLayoutManager(requireContext(), HORIZONTAL, false)
		bottom_problems_list.adapter = problemsMapAdapter

		val snapHelper = LinearSnapHelper()
		val snapOnScrollListener = SnapOnScrollListener(snapHelper, this,
			SnapOnScrollListener.Behavior.NOTIFY_ON_SCROLL_STATE_IDLE)

		snapHelper.attachToRecyclerView(bottom_problems_list)
		bottom_problems_list.addOnScrollListener(snapOnScrollListener)

		bottom_hide_button.setOnClickListener {
			when (bottomSheetBehavior.state) {
				BottomSheetBehavior.STATE_EXPANDED -> {
					bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
				}
				else -> {
					bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
				}
			}
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

		map_button_add_task clickReduce MapPointsAction.CreateTask
		map_show_as_list clickReduce MapPointsAction.ShowProblemsAsList
		map_menu_button clickReduce MapPointsAction.ShowMenu

		val animatorElevationDown = ObjectAnimator.ofFloat(
			map_button_add_task,
			"cardElevation",
			ScreenUtils.convertDpToPixel(4f),
			ScreenUtils.convertDpToPixel(8f)
		)

		val animatorElevationUp = ObjectAnimator.ofFloat(
			map_button_add_task,
			"cardElevation",
			ScreenUtils.convertDpToPixel(8f),
			ScreenUtils.convertDpToPixel(4f)
		)

		map_button_add_task.setOnTouchListener { _, event ->
			when (event.action) {
				MotionEvent.ACTION_DOWN -> {
					if (animatorElevationUp.isRunning || animatorElevationUp.isStarted) {
						animatorElevationUp.cancel()
					}
					animatorElevationDown.start()
					false
				}
				MotionEvent.ACTION_CANCEL,
				MotionEvent.ACTION_UP -> {
					if (animatorElevationDown.isRunning || animatorElevationDown.isStarted) {
						animatorElevationDown.cancel()
					}
					animatorElevationUp.start()
					false
				}
				else -> false
			}
		}
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		map_view.mapInteractionReady = {
			reducer.reduce(MapPointsAction.LoadMapPoints)
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

		when (state.authState) {
			AuthState.AUTH -> map_button_add_task.showWithScale()
			else -> map_button_add_task.hideWithScale()
		}
	}

	override fun onSnapPositionChange(position: Int) {
		val task = reducer.state.tasks[position]
		val screenQuarterWidth = ScreenUtils.screenWidth / 4
		map_view.animateCameraOffset(Location(task.latitude, task.longitude), screenQuarterWidth)
	}

	private fun initBottomState(view: View): BottomSheetBehavior<View> {
		val bottomSheetBehavior = BottomSheetBehavior.from(view)
		bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
		bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
			override fun onSlide(p0: View, slideOffset: Float) {}

			override fun onStateChanged(p0: View, state: Int) {
				when (state) {
					BottomSheetBehavior.STATE_COLLAPSED -> {
						bottom_hide_button.setImageResource(R.drawable.ic_expand_less_gray_24dp)
						map_button_add_task.showWithScale()
					}
					BottomSheetBehavior.STATE_EXPANDED -> {
						bottom_hide_button.setImageResource(R.drawable.ic_expand_more_gray_24dp)
						map_button_add_task.hideWithScale()
					}
				}
			}

		})
		return bottomSheetBehavior
	}
}
