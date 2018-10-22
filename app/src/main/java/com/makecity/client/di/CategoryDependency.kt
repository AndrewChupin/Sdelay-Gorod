package com.makecity.client.di

import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import com.makecity.client.data.category.*
import com.makecity.client.data.temp_problem.*
import com.makecity.client.presentation.category.CategoryData
import com.makecity.client.presentation.category.CategoryFragment
import com.makecity.client.presentation.category.CategoryReducer
import com.makecity.client.presentation.category.CategoryViewModel
import com.makecity.core.di.scope.FragmentScope
import com.makecity.core.domain.Mapper
import com.makecity.core.plugin.connection.ConnectionProvider
import com.makecity.core.presentation.viewmodel.ViewModelFactory
import com.makecity.core.utils.resources.ResourceManager
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import ru.terrakok.cicerone.Router


@FragmentScope
@Subcomponent(modules = [CategoryModule::class])
interface CategoryComponent {

	fun inject(fragment: CategoryFragment)

	@Subcomponent.Builder
	interface Builder {
		@BindsInstance
		fun withFragment(fragment: Fragment): Builder
		@BindsInstance
		fun withData(categoryData: CategoryData): Builder
		fun build(): CategoryComponent
	}

}


@Module
open class CategoryModule {

	@Provides
	@FragmentScope
	fun provideTempProblemMapperCommonToPersistence(
		mapperCommonToPersistence: TempProblemMapperCommonToPersistence
	): Mapper<TempProblem, TempProblemPersistence> = mapperCommonToPersistence

	@Provides
	@FragmentScope
	fun provideTempProblemMapperPersistenceToCommon(
		mapperPersistenceToCommon: TempProblemMapperPersistenceToCommon
	): Mapper<TempProblemPersistence, TempProblem> = mapperPersistenceToCommon

	@Provides
	@FragmentScope
	fun provideTempProblemStorage(problemStorageRoom: TempProblemStorageRoom): TempProblemStorage = problemStorageRoom


	@Provides
	@FragmentScope
	fun provideTempProblemDataSource(
		mapperCommonToPersistence: TempProblemMapperCommonToPersistence,
		mapperPersistenceToCommon: TempProblemMapperPersistenceToCommon,
		storage: TempProblemStorage
	): TempProblemDataSource = TempProblemDataSourceDefault(storage, mapperCommonToPersistence, mapperPersistenceToCommon)

	@Provides
	@FragmentScope
	fun provideCategoryService(categoryServiceDefault: CategoryServiceDefault): CategoryService = categoryServiceDefault

	@Provides
	@FragmentScope
	fun provideCategoryDataSource(
		categoryService: CategoryService,
		mapperDto: CategoryMapperDtoToPersist,
		mapperPersist: CategoryMapperPersistToCommon // TODO MAPPER
	): CategoryDataSource = CategoryDataSourceDefault(categoryService, mapperDto, mapperPersist)

	@Provides
	@FragmentScope
	fun provideViewModelFactory(
		router: Router,
		categoryData: CategoryData,
		resourceManager: ResourceManager,
		connectionProvider: ConnectionProvider,
		categoryDataSource: CategoryDataSource,
		tempProblemDataSource: TempProblemDataSource
	): CategoryViewModel = CategoryViewModel(router, categoryData, connectionProvider, resourceManager, categoryDataSource, tempProblemDataSource)

	@Provides
	@FragmentScope
	fun provideContract(
		fragment: Fragment,
		factory: ViewModelFactory<CategoryViewModel>
	): CategoryReducer = ViewModelProviders.of(fragment, factory).get(CategoryViewModel::class.java)
}