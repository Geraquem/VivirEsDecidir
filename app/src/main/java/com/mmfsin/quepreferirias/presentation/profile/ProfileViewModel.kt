package com.mmfsin.quepreferirias.presentation.profile

import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.mmfsin.quepreferirias.base.BaseViewModel
import com.mmfsin.quepreferirias.domain.usecases.CloseSessionUseCase
import com.mmfsin.quepreferirias.domain.usecases.GetSessionUseCase
import com.mmfsin.quepreferirias.domain.usecases.GoogleLoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getSessionUseCase: GetSessionUseCase,
    private val googleLoginUseCase: GoogleLoginUseCase,
    private val closeSessionUseCase: CloseSessionUseCase
) : BaseViewModel<ProfileEvent>() {

    fun getSession() {
        executeUseCase(
            { getSessionUseCase.execute() },
            { result ->
                _event.value = result?.let { ProfileEvent.Profile(result) } ?: ProfileEvent.SWW
            },
            { _event.value = ProfileEvent.SWW }
        )
    }

    fun closeSession() {
        executeUseCase(
            { googleLoginUseCase.execute() },
            { result -> deleteSessionFromSystem(result) },
            { _event.value = ProfileEvent.SWW }
        )
    }

    private fun deleteSessionFromSystem(client: GoogleSignInClient?) {
        executeUseCase(
            { closeSessionUseCase.execute(CloseSessionUseCase.Params(client)) },
            { _event.value = ProfileEvent.SessionClosed },
            { _event.value = ProfileEvent.SWW })
    }
}