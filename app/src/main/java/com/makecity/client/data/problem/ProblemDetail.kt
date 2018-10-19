package com.makecity.client.data.problem

import com.makecity.client.data.comments.Comment
import com.makecity.client.data.task.Task


data class ProblemDetail(
	val task: Task,
	val comments: List<Comment>
)