package com.mmfsin.quepreferirias.presentation.dashboard.dilemmas.comments.dialogs.send

sealed class SendCommentEvent {
    object CommentSent : SendCommentEvent()
    object SWW : SendCommentEvent()
}