package com.mmfsin.quepreferirias.presentation.ideas.myideas.dilemmas

import com.mmfsin.quepreferirias.base.BaseViewModel
import com.mmfsin.quepreferirias.domain.usecases.DeleteMyDataUseCase
import com.mmfsin.quepreferirias.domain.usecases.GetMyDilemmasUseCase
import com.mmfsin.quepreferirias.presentation.models.DashboardType.DILEMMA
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyDilemmasViewModel @Inject constructor(
    private val getMyDilemmasUseCase: GetMyDilemmasUseCase,
    private val deleteMyDataUseCase: DeleteMyDataUseCase
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
            { deleteMyDataUseCase.execute(DeleteMyDataUseCase.Params(dilemmaId, DILEMMA)) },
            { _event.value = MyDilemmasEvent.Deleted },
            { _event.value = MyDilemmasEvent.SWW }
        )
    }
}