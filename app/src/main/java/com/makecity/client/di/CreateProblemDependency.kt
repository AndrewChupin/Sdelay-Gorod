package com.makecity.client.di

import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import com.makecity.client.data.temp_problem.TempProblemDataSource
import com.makecity.client.presentation.create_problem.CreateProblemFragment
import com.makecity.client.presentation.create_problem.CreateProblemReducer
import com.makecity.client.presentation.create_problem.CreateProblemViewModel
import com.makecity.core.di.scope.FragmentScope
import com.makecity.core.plugin.connection.ConnectionProvider
import com.makecity.core.presentation.viewmodel.ViewModelFactory
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import ru.terrakok.cicerone.Router


@FragmentScope
@Subcomponent(modules = [CreateProblemModule::class])
interface CreateProblemComponent {

	fun inject(fragment: CreateProblemFragment)

	@Subcomponent.Builder
	interface Builder {
		@BindsInstance
		fun withFragment(fragment: Fragment): Builder
		fun build(): CreateProblemComponent
	}

}


@Module
open class CreateProblemModule {

	@Provides
	@FragmentScope
	fun provideViewModelFactory(
		router: Router,
		tempProblemDataSource: TempProblemDataSource,
		connectionProvider: ConnectionProvider
	): CreateProblemViewModel = CreateProblemViewModel(router, tempProblemDataSource, connectionProvider)

	@Provides
	@FragmentScope
	fun provideContract(
		fragment: Fragment,
		factory: ViewModelFactory<CreateProblemViewModel>
	): CreateProblemReducer = ViewModelProviders.of(fragment, factory).get(CreateProblemViewModel::class.java)
}