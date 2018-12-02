package com.makecity.client.di

import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import com.makecity.client.data.temp_problem.TempProblemDataSource
import com.makecity.client.presentation.restore.RestoreFragment
import com.makecity.client.presentation.restore.RestoreReducer
import com.makecity.client.presentation.restore.RestoreViewModel
import com.makecity.core.di.scope.FragmentScope
import com.makecity.core.presentation.viewmodel.ViewModelFactory
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import ru.terrakok.cicerone.Router


@FragmentScope
@Subcomponent(modules = [RestoreModule::class])
interface RestoreComponent {

	fun inject(fragment: RestoreFragment)

	@Subcomponent.Builder
	interface Builder {
		@BindsInstance
		fun withFragment(fragment: Fragment): Builder
		fun build(): RestoreComponent
	}

}


@Module
open class RestoreModule {

	@Provides
	@FragmentScope
	fun provideViewModelFactory(
		router: Router,
		tempProblemDataSource: TempProblemDataSource
	): RestoreViewModel = RestoreViewModel(router, tempProblemDataSource)

	@Provides
	@FragmentScope
	fun provideContract(
		fragment: Fragment,
		factory: ViewModelFactory<RestoreViewModel>
	): RestoreReducer = ViewModelProviders.of(fragment, factory).get(RestoreViewModel::class.java)
}