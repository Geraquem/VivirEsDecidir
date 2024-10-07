package com.mmfsin.quepreferirias.presentation.dashboard.dilemmas.interfaces

import com.mmfsin.quepreferirias.domain.models.CommentVote

interface ICommentsRVListener {
    fun onCommentNameClick(userId: String)
    fun openCommentMenu(commentId: String, userId: String)
    fun voteComment(commentId: String, vote: CommentVote, likes: Long, position: Int)
}