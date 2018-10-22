package com.makecity.client.presentation.category

import com.makecity.client.data.category.Category
import com.makecity.client.data.category.CategoryDataSource
import com.makecity.core.data.Presentation
import com.makecity.core.plugin.connection.ConnectionProvider
import com.makecity.core.plugin.connection.ConnectionState
import com.makecity.core.plugin.connection.ReducerPluginConnection
import com.makecity.core.presentation.state.PrimaryViewState
import com.makecity.core.presentation.state.StateLiveData
import com.makecity.core.presentation.state.ViewState
import com.makecity.core.presentation.viewmodel.ActionView
import com.makecity.core.presentation.viewmodel.BaseViewModel
import com.makecity.core.presentation.viewmodel.StatementReducer
import io.reactivex.disposables.CompositeDisposable
import ru.terrakok.cicerone.Router


// State
@Presentation
data class CategoryViewState(
	override val screenState: PrimaryViewState = PrimaryViewState.Loading,
	val categories: List<Category> = emptyList()
) : ViewState


// Action
sealed class CategoryAction: ActionView {
	object LoadCategories: CategoryAction()
}


// Reducer
interface CategoryReducer: StatementReducer<CategoryViewState, CategoryAction>


// ViewModel
class CategoryViewModel(
	private val router: Router,
	override val connectionProvider: ConnectionProvider,
	private val categoryDataSource: CategoryDataSource,
	override val disposables: CompositeDisposable = CompositeDisposable()
) : BaseViewModel(), CategoryReducer, ReducerPluginConnection {

	override val viewState: StateLiveData<CategoryViewState> = StateLiveData.create(CategoryViewState())

	override fun reduce(action: CategoryAction) {
		when (action) {
			is CategoryAction.LoadCategories -> categoryDataSource.getCategories()
				.bindSubscribe(onSuccess = {
					viewState.updateValue {
						copy(screenState = PrimaryViewState.Data, categories = it)
					}
				}, onError = {
					it.printStackTrace()
				})
		}
	}

	// IMPLEMENT - ConnectionPlugin
	override fun onChangeConnection(connectionState: ConnectionState) {

	}
}