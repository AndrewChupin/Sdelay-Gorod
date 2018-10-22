package com.makecity.client.di

import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import com.makecity.client.data.category.*
import com.makecity.client.presentation.category.CategoryFragment
import com.makecity.client.presentation.category.CategoryReducer
import com.makecity.client.presentation.category.CategoryViewModel
import com.makecity.core.di.scope.FragmentScope
import com.makecity.core.plugin.connection.ConnectionProvider
import com.makecity.core.presentation.viewmodel.ViewModelFactory
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
		fun build(): CategoryComponent
	}

}


@Module
open class CategoryModule {

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
		connectionProvider: ConnectionProvider,
		categoryDataSource: CategoryDataSource
	): CategoryViewModel = CategoryViewModel(router, connectionProvider, categoryDataSource)

	@Provides
	@FragmentScope
	fun provideContract(
		fragment: Fragment,
		factory: ViewModelFactory<CategoryViewModel>
	): CategoryReducer = ViewModelProviders.of(fragment, factory).get(CategoryViewModel::class.java)
}