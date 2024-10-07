package com.mmfsin.quepreferirias.presentation.dashboard.dilemmas.comments.send

sealed class SendCommentEvent {
    object CommentSent : SendCommentEvent()
    object SWW : SendCommentEvent()
}