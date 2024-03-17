package com.mmfsin.quepreferirias.presentation.send.dilemmas

import com.mmfsin.quepreferirias.base.BaseViewModel
import com.mmfsin.quepreferirias.domain.usecases.GetSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SendDilemmaViewModel @Inject constructor(
    private val getSessionUseCase: GetSessionUseCase,
) : BaseViewModel<SendDilemmaEvent>() {

    fun getUserData() {
        executeUseCase(
            { getSessionUseCase.execute() },
            { result -> _event.value = result?.let { SendDilemmaEvent.UserData(it) } },
            { _event.value = SendDilemmaEvent.SWW }
        )
    }
}