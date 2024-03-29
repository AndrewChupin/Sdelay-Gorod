package com.makecity.client.data.task

import com.makecity.client.BuildConfig
import com.makecity.client.data.comments.Author
import com.makecity.client.data.comments.AuthorPersistence
import com.makecity.client.utils.ImageHelper.concatImage
import com.makecity.core.domain.Mapper
import com.makecity.core.utils.Symbols
import com.makecity.core.utils.Symbols.EMPTY
import javax.inject.Inject


class ProblemMapperDtoToPersistence @Inject constructor() : Mapper<TaskRemote, TaskPersistence> {

	override fun transform(entity: TaskRemote) = entity.run {
		TaskPersistence(
			id = id,
			title = title ?: Symbols.EMPTY,
			text = text ?: Symbols.EMPTY,
			createdTime = createdTime * 1000,
			updatedTime = updatedTime * 1000,
			userId = userId,
			longitude = longitude,
			latitude = latitude,
			type = type ?: Symbols.EMPTY,
			imageFirst = imageFirst ?: Symbols.EMPTY,
			imageSecond = imageSecond ?: Symbols.EMPTY,
			video = video ?: Symbols.EMPTY,
			status = StatusPersistence(status?.id ?: 0L, status?.name ?: Symbols.EMPTY),
			address = address ?: Symbols.EMPTY,
			compliance = compliance,
			commentsCount = commentsCount,
			likeCounts = likeCounts,
			cityId = cityId,
			author = AuthorPersistence(
				author.userName ?: EMPTY,
				author.role ?: EMPTY,
				author.phone ?: EMPTY,
				author.image ?: EMPTY
			),
			isLiked = isLiked ?: false,
			categories = TaskCategoriesPersistence(
				main = TaskCategoryPersistence(
					categories.main?.id ?: -1L,
					categories.main?.name?.capitalize() ?: EMPTY
				),
				sub = categories.sub?.run { TaskCategoryPersistence(id, name.capitalize()) }
			),
			companyName = companies?.first()?.name ?: EMPTY,
			history = history?.map { concreteHistory ->
				concreteHistory.run {
					HistoryPersistence(
						id = id,
						text = text ?: EMPTY,
						createTime = createdTime,
						updateTime = updateTime,
						imageSecond = imageSecond ?: EMPTY,
						imageFirst = imageFirst ?: EMPTY,
						problemId = problemId
					)
				}
			} ?: emptyList()
		)
	}
}


class ProblemMapperPersistenceToCommon @Inject constructor() : Mapper<TaskPersistence, Task> {

	override fun transform(entity: TaskPersistence) = entity.run {
		Task(
			id = id,
			title = title,
			text = text,
			createdTime = createdTime,
			updatedTime = updatedTime,
			longitude = longitude,
			latitude = latitude,
			imageFirst = concatImage(BuildConfig.IMAGE_URL, imageFirst),
			imageSecond = concatImage(BuildConfig.IMAGE_URL, imageSecond),
			status = status.name,
			commentsCount = commentsCount,
			likeCounts = likeCounts,
			address = address,
			isLiked = isLiked,
			statusType = when (status.id) {
				1L -> ProblemStatus.NEW
				2L -> ProblemStatus.IN_PROGRESS
				3L -> ProblemStatus.DONE
				4L -> ProblemStatus.CANCELED
				5L -> ProblemStatus.REJECT
				else -> throw IllegalStateException("Status with id ${status.id} not existing")
			},
			author = Author(
				author.userName,
				author.role,
				author.phone,
				concatImage(BuildConfig.IMAGE_URL, author.image)
			),
			categories = TaskCategories(
				main = TaskCategory(
					categories.main.id,
					categories.main.name
				),
				sub = categories.sub?.run { TaskCategory(id, name) }
			),
			companyName = companyName,
			history = history.map { concreteHistory ->
				concreteHistory.run {
					History(
						id = id,
						text = text,
						createTime = createdTime,
						updateTime = updateTime,
						imageSecond = imageSecond,
						imageFirst = imageFirst,
						problemId = problemId
					)
				}
			}
		)
	}
}
