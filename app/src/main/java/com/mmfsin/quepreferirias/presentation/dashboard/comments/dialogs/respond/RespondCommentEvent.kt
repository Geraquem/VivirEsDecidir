package com.mmfsin.quepreferirias.presentation.dashboard.comments.dialogs.respond

import com.mmfsin.quepreferirias.domain.models.CommentReply

sealed class RespondCommentEvent {
    class CommentReplied(val reply: CommentReply) : RespondCommentEvent()
    object SWW : RespondCommentEvent()
}