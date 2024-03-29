package com.makecity.client.di

import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import com.makecity.client.presentation.about.AboutFragment
import com.makecity.client.presentation.about.AboutReducer
import com.makecity.client.presentation.about.AboutViewModel
import com.makecity.core.di.scope.FragmentScope
import com.makecity.core.presentation.viewmodel.ViewModelFactory
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import ru.terrakok.cicerone.Router


@FragmentScope
@Subcomponent(modules = [AboutModule::class])
interface AboutComponent {

	fun inject(mapPointsFragment: AboutFragment)

	@Subcomponent.Builder
	interface Builder {
		@BindsInstance
		fun withFragment(fragment: Fragment): Builder

		fun build(): AboutComponent
	}

}


@Module
open class AboutModule {

	@Provides
	@FragmentScope
	fun provideViewModelFactory(
		router: Router
	): AboutViewModel = AboutViewModel(router)

	@Provides
	@FragmentScope
	fun provideContract(
		fragment: Fragment,
		factory: ViewModelFactory<AboutViewModel>
	): AboutReducer = ViewModelProviders.of(fragment, factory).get(AboutViewModel::class.java)
}