package com.makecity.client.domain.map

import com.makecity.client.data.problem.ProblemDetail
import com.makecity.client.data.task.ProblemDataSource
import com.makecity.client.data.task.Task
import com.makecity.client.data.temp_problem.TempProblemDataSource
import io.reactivex.Single
import javax.inject.Inject


interface TaskPointsInteractor {
	fun refreshProblems(): Single<List<Task>>
	fun getProblems(): Single<List<Task>>
	fun loadProblemComments(problemId: Long): Single<ProblemDetail>
}


class TaskInteractorReactive @Inject constructor(
	private val problemDataSource: ProblemDataSource
): TaskPointsInteractor {

	override fun refreshProblems(): Single<List<Task>> = Single.defer {
		problemDataSource.refreshProblems()
	}

	override fun getProblems(): Single<List<Task>> = Single.defer {
		problemDataSource.getProblems()
	}
	override fun loadProblemComments(problemId: Long): Single<ProblemDetail> = Single.defer {
		problemDataSource.getProblemComments(problemId)
	}
}