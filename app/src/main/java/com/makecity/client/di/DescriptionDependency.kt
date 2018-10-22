package com.makecity.client.di

import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import com.makecity.client.presentation.description.DescriptionFragment
import com.makecity.client.presentation.description.DescriptionReducer
import com.makecity.client.presentation.description.DescriptionViewModel
import com.makecity.core.di.scope.FragmentScope
import com.makecity.core.plugin.connection.ConnectionProvider
import com.makecity.core.presentation.viewmodel.ViewModelFactory
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import ru.terrakok.cicerone.Router


@FragmentScope
@Subcomponent(modules = [DescriptionModule::class])
interface DescriptionComponent {

	fun inject(fragment: DescriptionFragment)

	@Subcomponent.Builder
	interface Builder {
		@BindsInstance
		fun withFragment(fragment: Fragment): Builder
		fun build(): DescriptionComponent
	}

}


@Module
open class DescriptionModule {

	@Provides
	@FragmentScope
	fun provideViewModelFactory(
		router: Router,
		connectionProvider: ConnectionProvider
	): DescriptionViewModel = DescriptionViewModel(router, connectionProvider)

	@Provides
	@FragmentScope
	fun provideContract(
		fragment: Fragment,
		factory: ViewModelFactory<DescriptionViewModel>
	): DescriptionReducer = ViewModelProviders.of(fragment, factory).get(DescriptionViewModel::class.java)
}