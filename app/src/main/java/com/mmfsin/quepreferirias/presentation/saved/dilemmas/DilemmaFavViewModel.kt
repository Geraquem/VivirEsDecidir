package com.mmfsin.quepreferirias.presentation.saved.dilemmas

import com.mmfsin.quepreferirias.base.BaseViewModel
import com.mmfsin.quepreferirias.domain.usecases.GetFavDilemmasUseCase
import com.mmfsin.quepreferirias.domain.usecases.InitiatedSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DilemmaFavViewModel @Inject constructor(
    private val initiatedSessionUseCase: InitiatedSessionUseCase,
    private val getFavDilemmasUseCase: GetFavDilemmasUseCase
) : BaseViewModel<DilemmaFavDataEvent>() {

    fun checkSessionInitiated() {
        executeUseCase(
            { initiatedSessionUseCase.execute() },
            { result -> _event.value = DilemmaFavDataEvent.InitiatedSession(result) },
            { _event.value = DilemmaFavDataEvent.SWW }
        )
    }

    fun getFavData() {
        executeUseCase(
            { getFavDilemmasUseCase.execute() },
            { result -> _event.value = DilemmaFavDataEvent.Data(result) },
            { _event.value = DilemmaFavDataEvent.SWW }
        )
    }
}