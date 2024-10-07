package com.mmfsin.quepreferirias.presentation.dashboard.dilemmas.listener

import com.mmfsin.quepreferirias.domain.models.Comment

interface ISendCommentListener {
    fun commentSent(comment: Comment)
}