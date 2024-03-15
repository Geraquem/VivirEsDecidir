package com.mmfsin.quepreferirias.presentation.saved

import com.mmfsin.quepreferirias.base.BaseViewModel
import com.mmfsin.quepreferirias.domain.usecases.GetFavDilemmasUseCase
import com.mmfsin.quepreferirias.domain.usecases.InitiatedSessionUseCase
import com.mmfsin.quepreferirias.domain.usecases.SetFavDilemmaUseCase
import com.mmfsin.quepreferirias.presentation.dashboard.dilemmas.DilemmasEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SavedDataViewModel @Inject constructor(
    private val initiatedSessionUseCase: InitiatedSessionUseCase,
    private val getFavDilemmasUseCase: GetFavDilemmasUseCase
) : BaseViewModel<SavedDataEvent>() {

    fun checkSessionInitiated() {
        executeUseCase(
            { initiatedSessionUseCase.execute() },
            { result -> _event.value = SavedDataEvent.InitiatedSession(result) },
            { _event.value = SavedDataEvent.SWW }
        )
    }

    fun getFavData() {
        executeUseCase(
            { getFavDilemmasUseCase.execute() },
            { result -> _event.value = SavedDataEvent.Data(result) },
            { _event.value = SavedDataEvent.SWW }
        )
    }
}