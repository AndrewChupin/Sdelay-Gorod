package com.makecity.client.di

import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import com.makecity.client.presentation.own_problems.OwnProblemsFragment
import com.makecity.client.presentation.own_problems.OwnProblemsReducer
import com.makecity.client.presentation.own_problems.OwnProblemsViewModel
import com.makecity.core.di.scope.FragmentScope
import com.makecity.core.plugin.connection.ConnectionProvider
import com.makecity.core.presentation.viewmodel.ViewModelFactory
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import ru.terrakok.cicerone.Router


@FragmentScope
@Subcomponent(modules = [OwnProblemsModule::class])
interface OwnProblemsComponent {

	fun inject(fragment: OwnProblemsFragment)

	@Subcomponent.Builder
	interface Builder {
		@BindsInstance
		fun withFragment(fragment: Fragment): Builder

		fun build(): OwnProblemsComponent
	}

}


@Module
open class OwnProblemsModule {

	@Provides
	@FragmentScope
	fun provideViewModelFactory(
		router: Router,
		connectionProvider: ConnectionProvider
	): OwnProblemsViewModel = OwnProblemsViewModel(router, connectionProvider)

	@Provides
	@FragmentScope
	fun provideContract(
		fragment: Fragment,
		factory: ViewModelFactory<OwnProblemsViewModel>
	): OwnProblemsReducer = ViewModelProviders.of(fragment, factory).get(OwnProblemsViewModel::class.java)
}