package com.makecity.client.di

import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import com.makecity.client.data.profile.ProfileDataSource
import com.makecity.client.presentation.menu.MenuFragment
import com.makecity.client.presentation.menu.MenuReducer
import com.makecity.client.presentation.menu.MenuViewModel
import com.makecity.core.di.scope.FragmentScope
import com.makecity.core.plugin.connection.ConnectionProvider
import com.makecity.core.presentation.viewmodel.ViewModelFactory
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import ru.terrakok.cicerone.Router


@FragmentScope
@Subcomponent(modules = [MenuModule::class])
interface MenuComponent {

	fun inject(mapPointsFragment: MenuFragment)

	@Subcomponent.Builder
	interface Builder {
		@BindsInstance
		fun withFragment(fragment: Fragment): Builder

		fun build(): MenuComponent
	}

}


@Module
open class MenuModule {

	@Provides
	@FragmentScope
	fun provideViewModelFactory(
		router: Router,
		profileDataSource: ProfileDataSource,
		connectionProvider: ConnectionProvider
	): MenuViewModel = MenuViewModel(router, profileDataSource, connectionProvider)

	@Provides
	@FragmentScope
	fun provideContract(
		fragment: Fragment,
		factory: ViewModelFactory<MenuViewModel>
	): MenuReducer = ViewModelProviders.of(fragment, factory).get(MenuViewModel::class.java)
}
