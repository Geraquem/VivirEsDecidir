package com.mmfsin.quepreferirias.presentation.login

import com.mmfsin.quepreferirias.base.BaseViewModel
import com.mmfsin.quepreferirias.domain.models.Session
import com.mmfsin.quepreferirias.domain.usecases.GoogleLoginUseCase
import com.mmfsin.quepreferirias.domain.usecases.SaveSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val googleLoginUseCase: GoogleLoginUseCase,
    private val saveSessionUseCase: SaveSessionUseCase
) : BaseViewModel<LoginEvent>() {

    fun googleLogin() {
        executeUseCase(
            { googleLoginUseCase.execute() },
            { result -> _event.value = LoginEvent.GoogleClient(result) },
            { _event.value = LoginEvent.SWW }
        )
    }

    fun saveSession(email: String, name: String) {
        executeUseCase(
            { saveSessionUseCase.execute(SaveSessionUseCase.Params(Session(email, name))) },
            { _event.value = LoginEvent.SessionSaved },
            { _event.value = LoginEvent.SWW }
        )
    }
}