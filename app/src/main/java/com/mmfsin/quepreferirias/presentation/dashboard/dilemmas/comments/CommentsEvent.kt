package com.mmfsin.quepreferirias.presentation.dashboard.dilemmas.comments

import com.mmfsin.quepreferirias.domain.models.Comment
import com.mmfsin.quepreferirias.domain.models.Session

sealed class CommentsEvent {
    class GetUserData(val data: Session) : CommentsEvent()
    class Comments(val comments: List<Comment>) : CommentsEvent()
    class CommentSentResult(val result: Boolean) : CommentsEvent()
    object SWW : CommentsEvent()
}