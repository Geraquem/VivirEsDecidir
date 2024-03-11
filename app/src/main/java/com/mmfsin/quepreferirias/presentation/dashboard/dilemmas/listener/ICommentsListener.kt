package com.mmfsin.quepreferirias.presentation.dashboard.dilemmas.listener

import com.mmfsin.quepreferirias.domain.models.CommentVote

interface ICommentsListener {
    fun addNewComment()
    fun respondComment()
    fun voteComment(commentId: String, vote: CommentVote, likes: Long, position: Int)
}