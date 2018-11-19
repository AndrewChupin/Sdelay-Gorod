package com.makecity.client.di

import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import com.makecity.client.data.category.*
import com.makecity.client.data.company.*
import com.makecity.client.data.temp_problem.*
import com.makecity.client.presentation.category.CategoryScreenData
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
@Subcomponent(modules = [CategoryModule::class, CompanyModule::class])
interface CategoryComponent {

	fun inject(fragment: CategoryFragment)

	@Subcomponent.Builder
	interface Builder {
		@BindsInstance
		fun withFragment(fragment: Fragment): Builder
		@BindsInstance
		fun withData(categoryData: CategoryScreenData): Builder
		fun build(): CategoryComponent
	}

}


@Module
open class CompanyModule {

	@Provides
	@FragmentScope
	fun provideCompanyService(service: CompanyServiceRetrofit): CompanyService = service

	@Provides
	@FragmentScope
	fun provideCompanyDataSource(source: CompanyDataSourceDefault): CompanyDataSource = source

	@Provides
	@FragmentScope
	fun provideMapper(mapper: CompanyMapperDtoToCommon): Mapper<CompanyRemote, Company> = mapper
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
		mapperPersist: CategoryMapperPersistToCommon // TODO LATE MAPPER
	): CategoryDataSource = CategoryDataSourceDefault(categoryService, mapperDto, mapperPersist)

	@Provides
	@FragmentScope
	fun provideViewModelFactory(
		router: Router,
		companyDataSource: CompanyDataSource,
		categoryData: CategoryScreenData,
		resourceManager: ResourceManager,
		connectionProvider: ConnectionProvider,
		categoryDataSource: CategoryDataSource,
		tempProblemDataSource: TempProblemDataSource
	): CategoryViewModel = CategoryViewModel(router, categoryData, companyDataSource, connectionProvider, resourceManager, categoryDataSource, tempProblemDataSource)

	@Provides
	@FragmentScope
	fun provideContract(
		fragment: Fragment,
		factory: ViewModelFactory<CategoryViewModel>
	): CategoryReducer = ViewModelProviders.of(fragment, factory).get(CategoryViewModel::class.java)
}