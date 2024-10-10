package com.mmfsin.quepreferirias.presentation.initial

import com.mmfsin.quepreferirias.base.BaseViewModel
import com.mmfsin.quepreferirias.domain.usecases.CheckIfUserIdIsMeUseCase
import com.mmfsin.quepreferirias.domain.usecases.GetSessionUseCase
import com.mmfsin.quepreferirias.domain.usecases.InitiatedSessionUseCase
import com.mmfsin.quepreferirias.presentation.dashboard.dualisms.DualismsEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InitialViewModel @Inject constructor(
    private val initiatedSessionUseCase: InitiatedSessionUseCase,
    private val getSessionUseCase: GetSessionUseCase,
) : BaseViewModel<InitialEvent>() {

    fun checkSessionInitiated() {
        executeUseCase(
            { initiatedSessionUseCase.execute() },
            { result -> _event.value = InitialEvent.InitiatedSession(result) },
            { _event.value = InitialEvent.SWW }
        )
    }

    fun reCheckSession() {
        executeUseCase(
            { initiatedSessionUseCase.execute() },
            { result -> _event.value = InitialEvent.ReCheckSession(result) },
            { _event.value = InitialEvent.SWW }
        )
    }

    fun getSession() {
        executeUseCase(
            { getSessionUseCase.execute() },
            { result -> _event.value = InitialEvent.GetSession(result) },
            { _event.value = InitialEvent.SWW }
        )
    }

}