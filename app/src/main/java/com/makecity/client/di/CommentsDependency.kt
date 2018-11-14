package com.makecity.client.di

import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import com.makecity.client.presentation.comments.CommentsFragment
import com.makecity.client.presentation.comments.CommentsReducer
import com.makecity.client.presentation.comments.CommentsViewModel
import com.makecity.core.di.scope.FragmentScope
import com.makecity.core.plugin.connection.ConnectionProvider
import com.makecity.core.presentation.list.paging.PagingActionsAdapter
import com.makecity.core.presentation.viewmodel.ViewModelFactory
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import ru.terrakok.cicerone.Router


@FragmentScope
@Subcomponent(modules = [CommentsModule::class])
interface CommentsComponent {

	fun inject(fragment: CommentsFragment)

	@Subcomponent.Builder
	interface Builder {
		@BindsInstance
		fun withFragment(fragment: Fragment): Builder

		fun build(): CommentsComponent
	}
}


@Module
open class CommentsModule {

	@Provides
	@FragmentScope
	fun provideViewModelFactory(
		router: Router,
		pagingAdapter: PagingActionsAdapter,
		connectionProvider: ConnectionProvider
	): CommentsViewModel = CommentsViewModel(router, pagingAdapter, connectionProvider)

	@Provides
	@FragmentScope
	fun provideContract(
		fragment: Fragment,
		factory: ViewModelFactory<CommentsViewModel>
	): CommentsReducer = ViewModelProviders.of(fragment, factory).get(CommentsViewModel::class.java)
}

