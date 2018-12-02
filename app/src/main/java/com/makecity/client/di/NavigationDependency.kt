package com.makecity.client.di

import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import com.makecity.client.presentation.notification.NotificationFragment
import com.makecity.client.presentation.notification.NotificationReducer
import com.makecity.client.presentation.notification.NotificationViewModel
import com.makecity.core.di.scope.FragmentScope
import com.makecity.core.plugin.connection.ConnectionProvider
import com.makecity.core.presentation.viewmodel.ViewModelFactory
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import ru.terrakok.cicerone.Router


@FragmentScope
@Subcomponent(modules = [NotificationModule::class])
interface NotificationComponent {

	fun inject(mapPointsFragment: NotificationFragment)

	@Subcomponent.Builder
	interface Builder {
		@BindsInstance
		fun withFragment(fragment: Fragment): Builder

		fun build(): NotificationComponent
	}

}


@Module
open class NotificationModule {

	@Provides
	@FragmentScope
	fun provideViewModelFactory(
		router: Router,
		connectionProvider: ConnectionProvider
	): NotificationViewModel = NotificationViewModel(router, connectionProvider)

	@Provides
	@FragmentScope
	fun provideContract(
		fragment: Fragment,
		factory: ViewModelFactory<NotificationViewModel>
	): NotificationReducer = ViewModelProviders.of(fragment, factory).get(NotificationViewModel::class.java)
}
