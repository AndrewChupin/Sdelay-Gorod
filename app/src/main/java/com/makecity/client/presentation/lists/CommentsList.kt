package com.makecity.client.presentation.lists

import android.view.LayoutInflater
import android.view.ViewGroup
import com.makecity.client.R
import com.makecity.client.data.comments.Comment
import com.makecity.core.presentation.list.BaseMultiplyAdapter
import com.makecity.core.utils.image.ImageManager


class CommentsAdapter(
	private val imageManager: ImageManager,
	private val delegate: (Comment) -> Unit
): BaseMultiplyAdapter<Comment, CommentViewHolder>() {

	override var data: List<Comment> = emptyList()

	override fun onCreateViewHolder(viewGroup: ViewGroup, type: Int): CommentViewHolder = CommentViewHolder(
		containerView = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_comment, viewGroup, false),
		itemClickDelegate = delegate,
		imageManager = imageManager
	)

	override fun getItemCount(): Int = data.size

	override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
		holder.bind(data[position])
	}
}