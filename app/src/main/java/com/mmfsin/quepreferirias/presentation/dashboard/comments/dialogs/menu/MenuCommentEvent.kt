package com.mmfsin.quepreferirias.presentation.dashboard.comments.dialogs.menu

import com.mmfsin.quepreferirias.domain.models.Session

sealed class MenuCommentEvent {
    class UserData(val session: Session?) : MenuCommentEvent()
    class CommentDeleted(val result: Boolean) : MenuCommentEvent()
    object SWW : MenuCommentEvent()
}