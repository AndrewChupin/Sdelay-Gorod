package com.makecity.client.domain.map

import com.makecity.client.data.problem.ProblemDetail
import com.makecity.client.data.task.ProblemDataSource
import com.makecity.client.data.task.Task
import io.reactivex.Single
import javax.inject.Inject


interface TaskPointsInteractor {
	fun loadProblems(): Single<List<Task>>
	fun loadProblemComments(problemId: Long): Single<ProblemDetail>
}


class TaskInteractorReactive @Inject constructor(
	private val problemDataSource: ProblemDataSource
): TaskPointsInteractor {
	override fun loadProblems(): Single<List<Task>> = problemDataSource.getProblems(56L)
	override fun loadProblemComments(problemId: Long): Single<ProblemDetail>
		= problemDataSource.getProblemComments(problemId)
}