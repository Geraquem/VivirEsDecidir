package com.mmfsin.quepreferirias.presentation.dashboard.dualisms

import com.mmfsin.quepreferirias.base.BaseViewModel
import com.mmfsin.quepreferirias.domain.usecases.InitiatedSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DualismsViewModel @Inject constructor(
    private val initiatedSessionUseCase: InitiatedSessionUseCase
) : BaseViewModel<DualismsEvent>() {

    fun checkSessionInitiated() {
        executeUseCase(
            { initiatedSessionUseCase.execute() },
            { result -> _event.value = DualismsEvent.InitiatedSession(result) },
            { _event.value = DualismsEvent.SWW }
        )
    }

    fun reCheckSession() {
        executeUseCase(
            { initiatedSessionUseCase.execute() },
            { result -> _event.value = DualismsEvent.ReCheckSession(result) },
            { _event.value = DualismsEvent.SWW }
        )
    }
}