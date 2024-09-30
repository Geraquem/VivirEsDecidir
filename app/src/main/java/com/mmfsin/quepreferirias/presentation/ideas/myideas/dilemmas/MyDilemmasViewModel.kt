package com.mmfsin.quepreferirias.presentation.ideas.myideas.dilemmas

import com.mmfsin.quepreferirias.base.BaseViewModel
import com.mmfsin.quepreferirias.domain.usecases.DeleteMyDilemmaUseCase
import com.mmfsin.quepreferirias.domain.usecases.GetMyDilemmasUseCase
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
            { result -> _event.value = MyDilemmasEvent.Dilemmas(result) },
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