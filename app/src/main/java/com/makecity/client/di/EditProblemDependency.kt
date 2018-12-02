package com.makecity.client.di

import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import com.makecity.client.presentation.edit_problem.EditProblemFragment
import com.makecity.client.presentation.edit_problem.EditProblemReducer
import com.makecity.client.presentation.edit_problem.EditProblemViewModel
import com.makecity.core.di.scope.FragmentScope
import com.makecity.core.plugin.connection.ConnectionProvider
import com.makecity.core.presentation.viewmodel.ViewModelFactory
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import ru.terrakok.cicerone.Router


@FragmentScope
@Subcomponent(modules = [EditProblemModule::class])
interface EditProblemComponent {

	fun inject(fragment: EditProblemFragment)

	@Subcomponent.Builder
	interface Builder {
		@BindsInstance
		fun withFragment(fragment: Fragment): Builder

		fun build(): EditProblemComponent
	}

}


@Module
open class EditProblemModule {

	@Provides
	@FragmentScope
	fun provideViewModelFactory(
		router: Router,
		connectionProvider: ConnectionProvider
	): EditProblemViewModel = EditProblemViewModel(router, connectionProvider)

	@Provides
	@FragmentScope
	fun provideContract(
		fragment: Fragment,
		factory: ViewModelFactory<EditProblemViewModel>
	): EditProblemReducer = ViewModelProviders.of(fragment, factory).get(EditProblemViewModel::class.java)
}