package com.mmfsin.quepreferirias.presentation.dashboard.dilemmas.comments.old

import com.mmfsin.quepreferirias.domain.models.Comment
import com.mmfsin.quepreferirias.domain.models.CommentVote
import com.mmfsin.quepreferirias.domain.models.Session

sealed class CommentsEvent {
    class InitiatedSession(val initiatedSession: Boolean) : CommentsEvent()
    class UserData(val data: Session) : CommentsEvent()

    class Comments(val comments: List<Comment>) : CommentsEvent()
    object CommentSentResult : CommentsEvent()
    object CommentAlreadyVoted : CommentsEvent()
    class CommentVotedResult(
        val vote: CommentVote,
        val position: Int,
        val alreadyVoted: Boolean
    ) : CommentsEvent()

    object SWW : CommentsEvent()
}