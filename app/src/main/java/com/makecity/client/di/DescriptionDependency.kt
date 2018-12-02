package com.makecity.client.di

import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import com.makecity.client.data.temp_problem.TempProblemDataSource
import com.makecity.client.presentation.description.DescriptionFragment
import com.makecity.client.presentation.description.DescriptionReducer
import com.makecity.client.presentation.description.DescriptionScreenData
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

		@BindsInstance
		fun withData(data: DescriptionScreenData): Builder

		fun build(): DescriptionComponent
	}

}


@Module
open class DescriptionModule {

	@Provides
	@FragmentScope
	fun provideViewModelFactory(
		router: Router,
		data: DescriptionScreenData,
		tempProblemDataSource: TempProblemDataSource,
		connectionProvider: ConnectionProvider
	): DescriptionViewModel = DescriptionViewModel(router, data, tempProblemDataSource, connectionProvider)

	@Provides
	@FragmentScope
	fun provideContract(
		fragment: Fragment,
		factory: ViewModelFactory<DescriptionViewModel>
	): DescriptionReducer = ViewModelProviders.of(fragment, factory).get(DescriptionViewModel::class.java)
}