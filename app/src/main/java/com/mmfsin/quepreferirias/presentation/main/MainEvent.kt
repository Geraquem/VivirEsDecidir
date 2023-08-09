package com.mmfsin.quepreferirias.presentation.main

import com.google.android.gms.auth.api.signin.GoogleSignInClient

sealed class MainEvent {
    class GoogleClient(val user: GoogleSignInClient?) : MainEvent()
    object SWW : MainEvent()
}