package com.mmfsin.quepreferirias.presentation.dashboard.dilemmas.comments

import com.mmfsin.quepreferirias.domain.models.Comment
import com.mmfsin.quepreferirias.domain.models.CommentVote
import com.mmfsin.quepreferirias.domain.models.Session

sealed class CommentsEvent {
    class Comments(val comments: List<Comment>) : CommentsEvent()
    object CommentAlreadyVoted : CommentsEvent()
    class CommentVotedResult(
        val vote: CommentVote,
        val position: Int,
        val alreadyVoted: Boolean
    ) : CommentsEvent()

    object SWW : CommentsEvent()
}