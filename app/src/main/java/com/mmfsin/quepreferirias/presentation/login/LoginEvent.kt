package com.mmfsin.quepreferirias.presentation.login

import com.google.android.gms.auth.api.signin.GoogleSignInClient

sealed class LoginEvent {
    class GoogleClient(val user: GoogleSignInClient?) : LoginEvent()
    data object SessionSaved : LoginEvent()
    data object SWW : LoginEvent()
}