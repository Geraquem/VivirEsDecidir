package com.mmfsin.quepreferirias.presentation.dashboard.comments.dialogs.menu.replies

import com.mmfsin.quepreferirias.domain.models.Session

sealed class MenuRepliesEvent {
    class UserData(val session: Session?) : MenuRepliesEvent()
    class ReplyDeleted(val result: Boolean) : MenuRepliesEvent()
    object SWW : MenuRepliesEvent()
}