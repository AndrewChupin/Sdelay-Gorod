package com.makecity.client.di

import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import com.makecity.client.presentation.filter.ProblemFilterFragment
import com.makecity.client.presentation.filter.ProblemFilterReducer
import com.makecity.client.presentation.filter.ProblemFilterViewModel
import com.makecity.core.di.scope.FragmentScope
import com.makecity.core.plugin.connection.ConnectionProvider
import com.makecity.core.presentation.viewmodel.ViewModelFactory
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import ru.terrakok.cicerone.Router


@FragmentScope
@Subcomponent(modules = [ProblemFilterModule::class])
interface ProblemFilterComponent {

	fun inject(fragment: ProblemFilterFragment)

	@Subcomponent.Builder
	interface Builder {
		@BindsInstance
		fun withFragment(fragment: Fragment): Builder

		fun build(): ProblemFilterComponent
	}

}


@Module
open class ProblemFilterModule {

	@Provides
	@FragmentScope
	fun provideViewModelFactory(
		router: Router,
		connectionProvider: ConnectionProvider
	): ProblemFilterViewModel = ProblemFilterViewModel(router, connectionProvider)

	@Provides
	@FragmentScope
	fun provideContract(
		fragment: Fragment,
		factory: ViewModelFactory<ProblemFilterViewModel>
	): ProblemFilterReducer = ViewModelProviders.of(fragment, factory).get(ProblemFilterViewModel::class.java)
}