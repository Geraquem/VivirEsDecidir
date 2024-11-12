package com.mmfsin.quepreferirias.presentation.dashboard.comments.interfaces

import com.mmfsin.quepreferirias.domain.models.DataToRespondComment

interface ICommentMenuListener {
    fun respondComment(dataToRespondComment: DataToRespondComment)
    fun commentDeleted(commentId: String)
    fun reportComment(commentId: String)
}