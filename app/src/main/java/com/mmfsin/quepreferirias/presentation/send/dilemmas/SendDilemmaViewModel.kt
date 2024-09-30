package com.mmfsin.quepreferirias.presentation.send.dilemmas

import com.mmfsin.quepreferirias.base.BaseViewModel
import com.mmfsin.quepreferirias.domain.usecases.GetSessionUseCase
import com.mmfsin.quepreferirias.domain.usecases.SendDilemmaUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SendDilemmaViewModel @Inject constructor(
    private val getSessionUseCase: GetSessionUseCase,
    private val sendDilemmaUseCase: SendDilemmaUseCase
) : BaseViewModel<SendDilemmaEvent>() {

    fun getUserData() {
        executeUseCase(
            { getSessionUseCase.execute() },
            { result -> _event.value = result?.let { SendDilemmaEvent.UserData(it) } },
            { _event.value = SendDilemmaEvent.SWW }
        )
    }

    fun sendDilemma(txtTop: String, txtBottom: String, creatorId: String, creatorName: String) {
        executeUseCase(
            {
                sendDilemmaUseCase.execute(
                    SendDilemmaUseCase.Params(txtTop, txtBottom, creatorId, creatorName)
                )
            },
            { _event.value = SendDilemmaEvent.DataSent },
            { _event.value = SendDilemmaEvent.SWW }
        )
    }
}