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

    fun saveSession(session: Session) {
        executeUseCase(
            { saveSessionUseCase.execute(SaveSessionUseCase.Params(session)) },
            { result -> _event.value = if (result) LoginEvent.SessionSaved else LoginEvent.SWW },
            { _event.value = LoginEvent.SWW }
        )
    }
}