package com.mmfsin.quepreferirias.presentation.myideas.dilemmas

import com.mmfsin.quepreferirias.base.BaseViewModel
import com.mmfsin.quepreferirias.domain.usecases.GetFavDilemmasUseCase
import com.mmfsin.quepreferirias.domain.usecases.GetMyDilemmasUseCase
import com.mmfsin.quepreferirias.domain.usecases.InitiatedSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyDilemmasViewModel @Inject constructor(
    private val getMyDilemmasUseCase: GetMyDilemmasUseCase
) : BaseViewModel<MyDilemmasEvent>() {

    fun getMyDilemmas() {
        executeUseCase(
            { getMyDilemmasUseCase.execute() },
            { result -> _event.value = MyDilemmasEvent.Data(result) },
            { _event.value = MyDilemmasEvent.SWW }
        )
    }
}