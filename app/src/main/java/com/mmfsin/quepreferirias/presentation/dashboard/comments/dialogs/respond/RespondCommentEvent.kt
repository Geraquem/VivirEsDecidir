package com.mmfsin.quepreferirias.presentation.dashboard.comments.dialogs.respond

import com.mmfsin.quepreferirias.domain.models.Comment

sealed class RespondCommentEvent {
    class CommentReplied(val comment: Comment) : RespondCommentEvent()
    object SWW : RespondCommentEvent()
}