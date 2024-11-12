package com.mmfsin.quepreferirias.presentation.dashboard.comments

import com.mmfsin.quepreferirias.domain.models.Comment
import com.mmfsin.quepreferirias.domain.models.CommentVote
import com.mmfsin.quepreferirias.domain.models.DataToRespondComment
import com.mmfsin.quepreferirias.domain.models.Session

sealed class CommentsEvent {
    class CheckIfSession(val hasSession: Boolean) : CommentsEvent()
    class Comments(val comments: List<Comment>) : CommentsEvent()
    object CommentAlreadyVoted : CommentsEvent()
    class CommentVotedResult(
        val vote: CommentVote,
        val position: Int,
        val alreadyVoted: Boolean
    ) : CommentsEvent()

    class GetSessionToRespondComment(
        val session: Session?,
        val data: DataToRespondComment
    ) : CommentsEvent()

    object CommentReported : CommentsEvent()
    object SWW : CommentsEvent()
}