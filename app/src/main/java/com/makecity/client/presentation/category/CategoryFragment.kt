package com.makecity.client.presentation.category

import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.View
import com.makecity.client.R
import com.makecity.client.app.AppInjector
import com.makecity.client.presentation.lists.CategoryAdapter
import com.makecity.core.extenstion.calculateDiffs
import com.makecity.core.extenstion.isVisible
import com.makecity.core.presentation.screen.ToolbarConfig
import com.makecity.core.presentation.screen.ToolbarScreen
import com.makecity.core.presentation.state.PrimaryViewState
import com.makecity.core.presentation.view.StatementFragment
import kotlinx.android.synthetic.main.fragment_category.*


typealias CategoryStatement = StatementFragment<CategoryReducer, CategoryViewState, CategoryAction>


class CategoryFragment : CategoryStatement(), ToolbarScreen {

	companion object {
		fun newInstance() = CategoryFragment()
	}

	private lateinit var adapter: CategoryAdapter

	override val layoutId: Int = R.layout.fragment_category

	override fun onInject() = AppInjector.inject(this)

	override fun getToolbar(): Toolbar = toolbar

	override fun onViewCreatedBeforeRender(savedInstanceState: Bundle?) {
		setupToolbarWith(requireActivity(), ToolbarConfig(
			title = "Выберите категорию",
			isDisplayHomeButton = true
		))
		category_recycler.layoutManager = LinearLayoutManager(requireContext())

		adapter = CategoryAdapter()
		category_recycler.adapter = adapter

	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		reducer.reduce(CategoryAction.LoadCategories)
	}

	override fun render(state: CategoryViewState) {
		when (state.screenState) {
			is PrimaryViewState.Data -> {
				adapter.calculateDiffs(state.categories)
				category_recycler.isVisible = true
			}
		}
	}
}