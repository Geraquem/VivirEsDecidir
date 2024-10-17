package com.mmfsin.quepreferirias.presentation.dashboard.comments.interfaces

import com.mmfsin.quepreferirias.domain.models.Comment

interface ISendCommentListener {
    fun commentSent(comment: Comment)
}