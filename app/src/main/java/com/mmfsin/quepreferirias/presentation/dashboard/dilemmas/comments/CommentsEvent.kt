package com.mmfsin.quepreferirias.presentation.dashboard.dilemmas.comments

import com.mmfsin.quepreferirias.domain.models.Comment
import com.mmfsin.quepreferirias.domain.models.CommentVote
import com.mmfsin.quepreferirias.domain.models.Session

sealed class CommentsEvent {
    class InitiatedSession(val initiatedSession: Boolean) : CommentsEvent()
    class GetUserData(val data: Session) : CommentsEvent()
    class Comments(val comments: List<Comment>) : CommentsEvent()
    object CommentSentResult : CommentsEvent()
    class CommentVotedResult(val vote: CommentVote, val position: Int) :
        CommentsEvent()

    object SWW : CommentsEvent()
}