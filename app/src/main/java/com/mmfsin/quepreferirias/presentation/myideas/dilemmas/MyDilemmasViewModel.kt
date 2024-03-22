package com.mmfsin.quepreferirias.presentation.myideas.dilemmas

import com.mmfsin.quepreferirias.base.BaseViewModel
import com.mmfsin.quepreferirias.domain.usecases.DeleteDilemmaFavUseCase
import com.mmfsin.quepreferirias.domain.usecases.DeleteMyDilemmaUseCase
import com.mmfsin.quepreferirias.domain.usecases.GetFavDilemmasUseCase
import com.mmfsin.quepreferirias.domain.usecases.GetMyDilemmasUseCase
import com.mmfsin.quepreferirias.domain.usecases.InitiatedSessionUseCase
import com.mmfsin.quepreferirias.presentation.saved.dilemmas.DilemmaFavEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyDilemmasViewModel @Inject constructor(
    private val getMyDilemmasUseCase: GetMyDilemmasUseCase,
    private val deleteMyDilemmaUseCase: DeleteMyDilemmaUseCase
) : BaseViewModel<MyDilemmasEvent>() {

    fun getMyDilemmas() {
        executeUseCase(
            { getMyDilemmasUseCase.execute() },
            { result -> _event.value = MyDilemmasEvent.Data(result) },
            { _event.value = MyDilemmasEvent.SWW }
        )
    }

    fun deleteMyDilemma(dilemmaId: String) {
        executeUseCase(
            { deleteMyDilemmaUseCase.execute(DeleteMyDilemmaUseCase.Params(dilemmaId)) },
            { _event.value = MyDilemmasEvent.Deleted },
            { _event.value = MyDilemmasEvent.SWW }
        )
    }
}