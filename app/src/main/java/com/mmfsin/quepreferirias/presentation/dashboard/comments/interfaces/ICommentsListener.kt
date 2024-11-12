package com.mmfsin.quepreferirias.presentation.dashboard.comments.interfaces

import com.mmfsin.quepreferirias.domain.models.DataToRespondComment

interface ICommentsListener {
    fun shouldInitiateSession()
    fun navigateToUserProfile(userId: String)
//    fun respondComment(dataToRespondComment: DataToRespondComment)
}