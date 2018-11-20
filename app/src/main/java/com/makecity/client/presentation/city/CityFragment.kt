package com.makecity.client.presentation.city

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import com.makecity.client.R
import com.makecity.client.app.AppInjector
import com.makecity.client.presentation.lists.CategoryAdapter
import com.makecity.core.extenstion.calculateDiffs
import com.makecity.core.presentation.screen.ToolbarConfig
import com.makecity.core.presentation.screen.ToolbarScreen
import com.makecity.core.presentation.state.PrimaryViewState
import com.makecity.core.presentation.view.StatementFragment
import kotlinx.android.synthetic.main.fragment_city.*


typealias CityStatement = StatementFragment<CityReducer, CityViewState, CityAction>


class CityFragment : CityStatement(), ToolbarScreen {

	private lateinit var adapter: CategoryAdapter

	companion object {
		fun newInstance() = CityFragment()
	}

	override val layoutId: Int = R.layout.fragment_city

	override fun onInject() = AppInjector.inject(this)

	override fun getToolbar(): Toolbar = toolbar

	override fun onViewCreatedBeforeRender(savedInstanceState: Bundle?) {
		setupToolbarWith(requireActivity(), ToolbarConfig(
			title = getString(R.string.loading_data)
		))

		adapter = CategoryAdapter {
			reducer.reduce(CityAction.SelectCity(it.first))
		}

		city_refresh.setOnRefreshListener {
			reducer.reduce(CityAction.RefreshCities)
		}

		city_recycler.layoutManager = LinearLayoutManager(context)
		city_recycler.adapter = adapter
	}

	override fun render(state: CityViewState) {
		when (state.screenState) {
			is PrimaryViewState.Loading -> {
				city_collapse_toolbar.title = getString(R.string.loading_data)
			}
			is PrimaryViewState.Data -> {
				city_collapse_toolbar.title = getString(R.string.cities)
				adapter.calculateDiffs(state.cities)
			}
		}
	}
}