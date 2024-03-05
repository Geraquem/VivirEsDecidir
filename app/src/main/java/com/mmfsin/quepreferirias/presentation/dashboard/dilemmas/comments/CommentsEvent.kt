package com.mmfsin.quepreferirias.presentation.dashboard.dilemmas.comments

import com.mmfsin.quepreferirias.domain.models.Session

sealed class CommentsEvent {
    class GetUserData(val data: Session) : CommentsEvent()
    class CommentResult(val result: Boolean) : CommentsEvent()
    object SWW : CommentsEvent()
}