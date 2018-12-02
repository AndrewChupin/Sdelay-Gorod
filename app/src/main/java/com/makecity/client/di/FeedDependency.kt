package com.makecity.client.di

import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import com.makecity.client.data.auth.AuthDataSource
import com.makecity.client.data.comments.CommentsDataSource
import com.makecity.client.data.task.ProblemDataSource
import com.makecity.client.domain.map.TaskInteractorReactive
import com.makecity.client.domain.map.TaskPointsInteractor
import com.makecity.client.presentation.feed.FeedFragment
import com.makecity.client.presentation.feed.FeedReducer
import com.makecity.client.presentation.feed.FeedViewModel
import com.makecity.core.di.scope.FragmentScope
import com.makecity.core.plugin.connection.ConnectionProvider
import com.makecity.core.presentation.viewmodel.ViewModelFactory
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import ru.terrakok.cicerone.Router


@FragmentScope
@Subcomponent(modules = [FeedModule::class])
interface FeedComponent {

	fun inject(mapPointsFragment: FeedFragment)

	@Subcomponent.Builder
	interface Builder {
		@BindsInstance
		fun withFragment(fragment: Fragment): Builder

		fun build(): FeedComponent
	}

}


@Module
open class FeedModule {

	@Provides
	@FragmentScope
	fun provideMapPointsInteractor(
		problemDataSource: ProblemDataSource,
		authDataSource: AuthDataSource,
		commentsDataSource: CommentsDataSource
	): TaskPointsInteractor = TaskInteractorReactive(problemDataSource, commentsDataSource, authDataSource)

	@Provides
	@FragmentScope
	fun provideViewModelFactory(
		router: Router,
		interactor: TaskPointsInteractor,
		connectionProvider: ConnectionProvider
	): FeedViewModel = FeedViewModel(router, interactor, connectionProvider)

	@Provides
	@FragmentScope
	fun provideContract(
		fragment: Fragment,
		factory: ViewModelFactory<FeedViewModel>
	): FeedReducer = ViewModelProviders.of(fragment, factory).get(FeedViewModel::class.java)
}