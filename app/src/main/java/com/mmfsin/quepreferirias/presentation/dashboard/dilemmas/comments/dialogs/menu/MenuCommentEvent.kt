package com.mmfsin.quepreferirias.presentation.dashboard.dilemmas.comments.dialogs.menu

import com.mmfsin.quepreferirias.domain.models.Session

sealed class MenuCommentEvent {
    class UserData(val session: Session?) : MenuCommentEvent()
    object SWW : MenuCommentEvent()
}