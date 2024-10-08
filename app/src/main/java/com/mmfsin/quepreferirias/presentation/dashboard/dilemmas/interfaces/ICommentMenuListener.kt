package com.mmfsin.quepreferirias.presentation.dashboard.dilemmas.interfaces

interface ICommentMenuListener {
    fun respondComment(commentId: String)
    fun commentDeleted(commentId: String)
    fun reportComment(commentId: String)
}