package com.mmfsin.quepreferirias.presentation.dashboard.dilemmas.listener

interface ICommentsListener {
    fun shouldInitiateSession()
    fun navigateToUserProfile(userId: String)
}