package com.makecity.client.di

import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import com.makecity.client.presentation.address.AddressFragment
import com.makecity.client.presentation.address.AddressReducer
import com.makecity.client.presentation.address.AddressViewModel
import com.makecity.core.di.scope.FragmentScope
import com.makecity.core.plugin.connection.ConnectionProvider
import com.makecity.core.presentation.viewmodel.ViewModelFactory
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import ru.terrakok.cicerone.Router


@FragmentScope
@Subcomponent(modules = [AddressModule::class])
interface AddressComponent {

	fun inject(fragment: AddressFragment)

	@Subcomponent.Builder
	interface Builder {
		@BindsInstance
		fun withFragment(fragment: Fragment): Builder

		fun build(): AddressComponent
	}

}


@Module
open class AddressModule {

	@Provides
	@FragmentScope
	fun provideViewModelFactory(
		router: Router,
		connectionProvider: ConnectionProvider
	): AddressViewModel = AddressViewModel(router, connectionProvider)

	@Provides
	@FragmentScope
	fun provideContract(
		fragment: Fragment,
		factory: ViewModelFactory<AddressViewModel>
	): AddressReducer = ViewModelProviders.of(fragment, factory).get(AddressViewModel::class.java)
}