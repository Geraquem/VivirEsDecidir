package com.mmfsin.quepreferirias.presentation.saved.dilemmas

import com.mmfsin.quepreferirias.base.BaseViewModel
import com.mmfsin.quepreferirias.domain.usecases.GetFavDilemmasUseCase
import com.mmfsin.quepreferirias.domain.usecases.InitiatedSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DilemmaFavViewModel @Inject constructor(
    private val getFavDilemmasUseCase: GetFavDilemmasUseCase
) : BaseViewModel<DilemmaFavEvent>() {

    fun getFavData() {
        executeUseCase(
            { getFavDilemmasUseCase.execute() },
            { result -> _event.value = DilemmaFavEvent.Data(result) },
            { _event.value = DilemmaFavEvent.SWW }
        )
    }
}