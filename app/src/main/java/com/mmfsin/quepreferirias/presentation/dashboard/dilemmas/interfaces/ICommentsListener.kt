package com.mmfsin.quepreferirias.presentation.dashboard.dilemmas.interfaces

interface ICommentsListener {
    fun shouldInitiateSession()
    fun navigateToUserProfile(userId: String)
}