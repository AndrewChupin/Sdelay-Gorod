package com.makecity.client.data.comments

import com.makecity.client.BuildConfig
import com.makecity.client.utils.ImageHelper.concatImage
import com.makecity.core.domain.Mapper
import com.makecity.core.utils.Symbols
import javax.inject.Inject


class CommentsAuthorMapperDtoToPersistence @Inject constructor() : Mapper<CommentRemote, CommentPersistence> {

	override fun transform(entity: CommentRemote): CommentPersistence = entity.run {
		CommentPersistence(
			id = id,
			text = text ?: Symbols.EMPTY,
			createdTime = createdTime * 1000,
			updatedTime = updatedTime * 1000,
			userId = userId,
			problemId = problemId,
			nameGuest = nameGuest ?: Symbols.EMPTY,
			status = status,
			author = author.run {
				AuthorPersistence(
					userName = userName ?: Symbols.EMPTY,
					role = role ?: Symbols.EMPTY,
					phone = phone ?: Symbols.EMPTY,
					image = image ?: Symbols.EMPTY
				)
			}
		)
	}
}

class CommentsAuthorMapperPersistenceToCommon @Inject constructor() : Mapper<CommentPersistence, Comment> {

	override fun transform(entity: CommentPersistence): Comment = entity.run {
		Comment(
			id = id,
			text = text,
			createdTime = createdTime,
			updatedTime = updatedTime,
			userId = userId,
			problemId = problemId,
			nameGuest = nameGuest,
			status = status,
			author = author.run {
				Author(
					userName = userName,
					role = role,
					phone = phone,
					image = concatImage(BuildConfig.IMAGE_URL, image)
				)
			}
		)
	}
}