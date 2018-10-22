package com.makecity.client.presentation.category

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.View
import com.makecity.client.R
import com.makecity.client.app.AppInjector
import com.makecity.client.presentation.lists.CategoryAdapter
import com.makecity.core.extenstion.calculateDiffs
import com.makecity.core.extenstion.isVisible
import com.makecity.core.extenstion.withArguments
import com.makecity.core.presentation.screen.ToolbarConfig
import com.makecity.core.presentation.screen.ToolbarScreen
import com.makecity.core.presentation.state.PrimaryViewState
import com.makecity.core.presentation.view.StatementFragment
import kotlinx.android.synthetic.main.fragment_category.*


typealias CategoryStatement = StatementFragment<CategoryReducer, CategoryViewState, CategoryAction>


class CategoryFragment : CategoryStatement(), ToolbarScreen {

	companion object {
		private const val ARGUMENT_CATEGORY_DATA = "ARGUMENT_CATEGORY_DATA"

		fun newInstance(categoryData: CategoryData) = CategoryFragment().withArguments {
			putParcelable(ARGUMENT_CATEGORY_DATA, categoryData)
		}
	}

	private lateinit var adapter: CategoryAdapter

	override val layoutId: Int = R.layout.fragment_category

	override fun onInject() = AppInjector.inject(this, getArgument(ARGUMENT_CATEGORY_DATA))

	override fun getToolbar(): Toolbar = toolbar

	override fun onViewCreatedBeforeRender(savedInstanceState: Bundle?) {
		setupToolbarWith(requireActivity(), ToolbarConfig(
			title = getString(R.string.loading_data),
			isDisplayHomeButton = true
		))
		category_recycler.layoutManager = LinearLayoutManager(requireContext())

		adapter = CategoryAdapter {
			reducer.reduce(CategoryAction.SelectItem(it.first))
		}
		category_recycler.adapter = adapter

	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		reducer.reduce(CategoryAction.LoadData)
	}

	override fun render(state: CategoryViewState) {
		category_collapse_toolbar.title = state.title

		when (state.screenState) {
			is PrimaryViewState.Data -> {
				adapter.calculateDiffs(state.entries)
				category_recycler.isVisible = true
			}
		}
	}
}