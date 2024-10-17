package com.mmfsin.quepreferirias.presentation.dashboard.comments.interfaces

interface ICommentMenuListener {
    fun respondComment(commentId: String)
    fun commentDeleted(commentId: String)
    fun reportComment(commentId: String)
}