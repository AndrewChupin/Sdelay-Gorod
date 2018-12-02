package com.makecity.core.di.module

import com.makecity.core.di.scope.FragmentScope
import com.makecity.core.presentation.list.paging.PagingActionsAdapter
import com.makecity.core.presentation.list.paging.PagingActionsAdapterDefault
import com.makecity.core.presentation.list.paging.PagingConfig
import dagger.Module
import dagger.Provides


@Module
class PagingModule {

	@Provides
	@FragmentScope
	fun providePagingConfig(): PagingConfig = PagingConfig(
		firstPageSize = 10,
		commonPageSize = 10,
		loadingBound = 10
	)

	@Provides
	@FragmentScope
	fun providePagingAction(adapter: PagingActionsAdapterDefault): PagingActionsAdapter = adapter

}
