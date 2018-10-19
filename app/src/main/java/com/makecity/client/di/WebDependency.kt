package com.makecity.client.di

import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import com.makecity.client.presentation.web.WebData
import com.makecity.client.presentation.web.WebFragment
import com.makecity.client.presentation.web.WebReducer
import com.makecity.client.presentation.web.WebViewModel
import com.makecity.core.di.scope.FragmentScope
import com.makecity.core.plugin.connection.ConnectionProvider
import com.makecity.core.presentation.viewmodel.ViewModelFactory
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import ru.terrakok.cicerone.Router


@FragmentScope
@Subcomponent(modules = [WebModule::class])
interface WebComponent {

	fun inject(mapPointsFragment: WebFragment)

	@Subcomponent.Builder
	interface Builder {
		@BindsInstance
		fun withFragment(fragment: Fragment): Builder

		@BindsInstance
		fun with(webData: WebData): Builder

		fun build(): WebComponent
	}

}


@Module
open class WebModule {

	@Provides
	@FragmentScope
	fun provideViewModelFactory(
		router: Router,
		webData: WebData,
		connectionProvider: ConnectionProvider
	): WebViewModel = WebViewModel(router, webData, connectionProvider)

	@Provides
	@FragmentScope
	fun provideContract(
		fragment: Fragment,
		factory: ViewModelFactory<WebViewModel>
	): WebReducer = ViewModelProviders.of(fragment, factory).get(WebViewModel::class.java)
}