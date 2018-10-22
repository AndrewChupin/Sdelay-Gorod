package com.makecity.client.di

import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import com.makecity.client.domain.auth.*
import com.makecity.client.presentation.auth.AuthData
import com.makecity.client.presentation.auth.AuthFragment
import com.makecity.client.presentation.auth.AuthReducer
import com.makecity.client.presentation.auth.AuthViewModel
import com.makecity.core.di.scope.FragmentScope
import com.makecity.core.domain.Validator
import com.makecity.core.plugin.connection.ConnectionProvider
import com.makecity.core.presentation.viewmodel.ViewModelFactory
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import ru.terrakok.cicerone.Router


@FragmentScope
@Subcomponent(modules = [AuthModule::class])
interface AuthComponent {

	fun inject(fragment: AuthFragment)

	@Subcomponent.Builder
	interface Builder {

		@BindsInstance
		fun withFragment(fragment: Fragment): Builder

		@BindsInstance
		fun withData(authData: AuthData): Builder

		fun build(): AuthComponent
	}
}


@Module
open class AuthModule {

	@Provides
	@FragmentScope
	fun provideValidator(validator: AuthContentValidator): Validator<AuthValidationRequest, AuthValidationResponse> = validator

	@Provides
	@FragmentScope
	fun provideAuthInteractor(authInteractorDefault: AuthInteractorDefault): AuthInteractor = authInteractorDefault

	@Provides
	@FragmentScope
	fun provideViewModelFactory(
		router: Router,
		authData: AuthData,
		connectionProvider: ConnectionProvider,
		authInteractorDefault: AuthInteractorDefault
	): AuthViewModel = AuthViewModel(router, authData, authInteractorDefault, connectionProvider)

	@Provides
	@FragmentScope
	fun provideContract(
		fragment: Fragment,
		factory: ViewModelFactory<AuthViewModel>
	): AuthReducer = ViewModelProviders.of(fragment, factory).get(AuthViewModel::class.java)
}