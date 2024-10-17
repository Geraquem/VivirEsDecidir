package com.mmfsin.quepreferirias.presentation.dashboard.comments.interfaces

interface ICommentsListener {
    fun shouldInitiateSession()
    fun navigateToUserProfile(userId: String)
}