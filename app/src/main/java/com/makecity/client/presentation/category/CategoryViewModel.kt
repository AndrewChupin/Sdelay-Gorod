package com.makecity.client.presentation.category

import android.os.Parcelable
import com.makecity.client.R
import com.makecity.client.app.AppScreens
import com.makecity.client.data.category.CategoryDataSource
import com.makecity.client.data.company.CompanyDataSource
import com.makecity.client.data.temp_problem.TempProblem
import com.makecity.client.data.temp_problem.TempProblemDataSource
import com.makecity.client.presentation.create_problem.ProblemCreatingType
import com.makecity.client.presentation.description.DescriptionScreenData
import com.makecity.core.data.Presentation
import com.makecity.core.extenstion.blockingCompletable
import com.makecity.core.plugin.connection.ConnectionProvider
import com.makecity.core.plugin.connection.ConnectionState
import com.makecity.core.plugin.connection.ReducerPluginConnection
import com.makecity.core.presentation.state.PrimaryViewState
import com.makecity.core.presentation.state.StateLiveData
import com.makecity.core.presentation.state.ViewState
import com.makecity.core.presentation.viewmodel.ActionView
import com.makecity.core.presentation.viewmodel.BaseViewModel
import com.makecity.core.presentation.viewmodel.StatementReducer
import com.makecity.core.utils.resources.ResourceManager
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.parcel.Parcelize
import ru.terrakok.cicerone.Router


// Data
@Parcelize
data class CategoryScreenData(
	val categoryType: CategoryType,
	val problemCreatingType: ProblemCreatingType
) : Parcelable


enum class CategoryType {
	CATEGORY, OPTION, COMPANY
}


// State
@Presentation
data class CategoryViewState(
	override val screenState: PrimaryViewState = PrimaryViewState.Loading,
	val entries: List<Pair<Long, String>> = emptyList(),
	val title: String
) : ViewState


// Action
sealed class CategoryAction : ActionView {
	object LoadData : CategoryAction()

	data class SelectItem(
		val pair: Pair<Long, String>
	) : CategoryAction()
}


// Reducer
interface CategoryReducer : StatementReducer<CategoryViewState, CategoryAction>


// ViewModel
class CategoryViewModel(
	private val router: Router,
	private val categoryData: CategoryScreenData,
	private val companyDataSource: CompanyDataSource,
	override val connectionProvider: ConnectionProvider,
	private val resourceManager: ResourceManager,
	private val categoryDataSource: CategoryDataSource,
	private val tempProblemDataSource: TempProblemDataSource,
	override val disposables: CompositeDisposable = CompositeDisposable()
) : BaseViewModel(), CategoryReducer, ReducerPluginConnection {

	override val viewState: StateLiveData<CategoryViewState> = StateLiveData.create(CategoryViewState(title = resourceManager.getString(R.string.loading_data)))

	init {
		reduce(CategoryAction.LoadData)
	}

	override fun reduce(action: CategoryAction) {
		when (action) {
			is CategoryAction.LoadData -> when (categoryData.categoryType) {
				CategoryType.CATEGORY -> loadCategory()
				CategoryType.OPTION -> loadOptions()
				CategoryType.COMPANY -> loadCompanies()
			}

			is CategoryAction.SelectItem -> when (categoryData.categoryType) {
				CategoryType.CATEGORY -> saveCategory(action.pair)
				CategoryType.OPTION -> saveOption(action.pair)
				CategoryType.COMPANY -> saveCompany(action.pair)
			}
		}
	}

	// MARK - CategoryAction.SelectItem
	private fun saveCategory(categoryPair: Pair<Long, String>) {
		tempProblemDataSource.getTempProblem()
			.map { it.copy(categoryId = categoryPair.first, categoryName = categoryPair.second) }
			.blockingCompletable(tempProblemDataSource::saveTempProblem)
			.map(TempProblem::categoryId)
			.flatMap(categoryDataSource::getCategory)
			.bindSubscribe(onSuccess = {
				when {
					it.options.isEmpty() -> router.navigateTo(
						AppScreens.CATEGORY_SCREEN_KEY,
						CategoryScreenData(CategoryType.COMPANY, categoryData.problemCreatingType)
					)
					else -> router.navigateTo(
						AppScreens.CATEGORY_SCREEN_KEY,
						CategoryScreenData(CategoryType.OPTION, categoryData.problemCreatingType)
					)
				}
			})
	}

	private fun saveOption(optionPair: Pair<Long, String>) {
		tempProblemDataSource.getTempProblem()
			.map { it.copy(optionId = optionPair.first, optionName = optionPair.second) }
			.flatMapCompletable(tempProblemDataSource::saveTempProblem)
			.bindSubscribe(onSuccess = {
				router.navigateTo(
					AppScreens.CATEGORY_SCREEN_KEY,
					CategoryScreenData(CategoryType.COMPANY, categoryData.problemCreatingType)
				)
			})
	}

	private fun saveCompany(companyPair: Pair<Long, String>) {
		tempProblemDataSource.getTempProblem()
			.map { it.copy(companyId = companyPair.first, companyName = companyPair.second) }
			.flatMapCompletable(tempProblemDataSource::saveTempProblem)
			.bindSubscribe(onSuccess = ::navigateComplete)
	}


	// MARK - CategoryAction.LoadData
	private fun loadCategory() {
		categoryDataSource.getCategories()
			.bindSubscribe(onSuccess = { data ->
				viewState.updateValue {
					copy(
						screenState = PrimaryViewState.Data,
						title = resourceManager.getString(R.string.choose_category),
						entries = data.map { Pair(it.id, it.name.capitalize()) }
					)
				}
			})
	}


	private fun loadOptions() {
		tempProblemDataSource.getTempProblem()
			.map(TempProblem::categoryId)
			.flatMap(categoryDataSource::getCategory)
			.bindSubscribe(onSuccess = { data ->
				viewState.updateValue {
					copy(
						screenState = PrimaryViewState.Data,
						title = data.name.capitalize(),
						entries = data.options.map { Pair(it.id, it.name.capitalize()) }
					)
				}
			}, onError = {
				it.printStackTrace()
			})
	}

	private fun loadCompanies() {
		companyDataSource.getCompanies()
			.bindSubscribe(onSuccess = { companies ->
				viewState.updateValue {
					copy(
						screenState = PrimaryViewState.Data,
						title = resourceManager.getString(R.string.choose_company),
						entries = companies.map { Pair(it.id, it.name) }
					)
				}
			})
	}


	// IMPLEMENT - ConnectionPlugin
	override fun onChangeConnection(connectionState: ConnectionState) {

	}

	private fun navigateComplete() {
		if (categoryData.problemCreatingType == ProblemCreatingType.NEW) {
			router.navigateTo(
				AppScreens.DESCRIPTION_SCREEN_KEY,
				DescriptionScreenData(ProblemCreatingType.NEW)
			)
		} else {
			router.backTo(AppScreens.CREATE_PROBLEM_SCREEN_KEY)
		}
	}
}