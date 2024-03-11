package com.mmfsin.quepreferirias.presentation.dashboard.dilemmas.listener

import com.mmfsin.quepreferirias.domain.models.Comment
import com.mmfsin.quepreferirias.domain.models.CommentVote

interface ICommentsListener {
    fun addNewComment()
    fun respondComment()
    fun voteComment(vote: CommentVote, comment: Comment, position: Int)
}