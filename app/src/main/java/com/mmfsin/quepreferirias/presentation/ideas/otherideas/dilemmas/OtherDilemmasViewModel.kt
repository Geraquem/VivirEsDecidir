package com.mmfsin.quepreferirias.presentation.ideas.otherideas.dilemmas

import com.mmfsin.quepreferirias.base.BaseViewModel
import com.mmfsin.quepreferirias.domain.usecases.GetOtherUserDilemmasUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OtherDilemmasViewModel @Inject constructor(
    private val getOtherUserDilemmasUseCase: GetOtherUserDilemmasUseCase,
) : BaseViewModel<OtherDilemmasEvent>() {

    fun getUserDilemmas(userId: String) {
        executeUseCase(
            { getOtherUserDilemmasUseCase.execute(GetOtherUserDilemmasUseCase.Params(userId)) },
            { result -> _event.value = OtherDilemmasEvent.Dilemmas(result) },
            { _event.value = OtherDilemmasEvent.SWW }
        )
    }
}