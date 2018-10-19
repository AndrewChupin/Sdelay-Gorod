package com.makecity.core.di.module

import com.makecity.core.di.scope.FragmentScope
import com.makecity.core.presentation.list.paging.*
import dagger.Module
import dagger.Provides


@Module
class PagingModule {

	@Provides
	@FragmentScope
	fun providePagingConfig(): PagingConfig = PagingConfig(
		firstPageSize = 30,
		commonPageSize = 20,
		loadingBound = 15
	)

	@Provides
	@FragmentScope
	fun providePagingAction(adapter: PagingActionsAdapterDefault): PagingActionsAdapter = adapter

}
