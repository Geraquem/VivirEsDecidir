package com.mmfsin.quepreferirias.presentation.dashboard.comments.dialogs.send

import com.mmfsin.quepreferirias.domain.models.Comment

sealed class SendCommentEvent {
    class CommentSent(val comment: Comment) : SendCommentEvent()
    object SWW : SendCommentEvent()
}