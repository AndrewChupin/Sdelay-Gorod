package com.makecity.client.di

import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import com.makecity.client.data.task.ProblemDataSource
import com.makecity.client.domain.map.TaskInteractorReactive
import com.makecity.client.domain.map.TaskPointsInteractor
import com.makecity.client.presentation.problem.ProblemData
import com.makecity.client.presentation.problem.ProblemFragment
import com.makecity.client.presentation.problem.ProblemReducer
import com.makecity.client.presentation.problem.ProblemViewModel
import com.makecity.core.di.scope.FragmentScope
import com.makecity.core.presentation.viewmodel.ViewModelFactory
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import ru.terrakok.cicerone.Router


@FragmentScope
@Subcomponent(modules = [ProblemModule::class])
interface ProblemComponent {

	fun inject(mapPointsFragment: ProblemFragment)

	@Subcomponent.Builder
	interface Builder {
		@BindsInstance
		fun withData(data: ProblemData): Builder

		@BindsInstance
		fun withFragment(fragment: Fragment): Builder

		fun build(): ProblemComponent
	}

}


@Module
open class ProblemModule {

	@Provides
	@FragmentScope
	fun provideMapPointsInteractor(problemDataSource: ProblemDataSource): TaskPointsInteractor = TaskInteractorReactive(problemDataSource)

	@Provides
	@FragmentScope
	fun provideViewModelFactory(
		router: Router,
		problemData: ProblemData,
		interactor: TaskPointsInteractor
	): ProblemViewModel = ProblemViewModel(router, problemData, interactor)

	@Provides
	@FragmentScope
	fun provideContract(
		fragment: Fragment,
		factory: ViewModelFactory<ProblemViewModel>
	): ProblemReducer = ViewModelProviders.of(fragment, factory).get(ProblemViewModel::class.java)
}