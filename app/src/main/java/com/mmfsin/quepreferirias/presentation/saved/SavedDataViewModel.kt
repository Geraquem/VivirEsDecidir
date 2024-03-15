package com.mmfsin.quepreferirias.presentation.saved

import com.mmfsin.quepreferirias.base.BaseViewModel
import com.mmfsin.quepreferirias.domain.usecases.InitiatedSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SavedDataViewModel @Inject constructor(
    private val initiatedSessionUseCase: InitiatedSessionUseCase
) : BaseViewModel<SavedDataEvent>() {

    fun favDilemma(dilemmaId: String, txtTop: String, txtBottom: String) {
//        executeUseCase(
//            {
//                setFavDilemmaUseCase.execute(
//                    SetFavDilemmaUseCase.Params(dilemmaId, txtTop, txtBottom)
//                )
//            },
//            { },
//            { _event.value = DilemmasEvent.SWW }
//        )
    }
}