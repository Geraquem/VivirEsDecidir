package com.mmfsin.quepreferirias.presentation.send.dualisms

import com.mmfsin.quepreferirias.base.BaseViewModel
import com.mmfsin.quepreferirias.domain.usecases.GetSessionUseCase
import com.mmfsin.quepreferirias.domain.usecases.SendDualismUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SendDualismsViewModel @Inject constructor(
    private val getSessionUseCase: GetSessionUseCase,
    private val sendDualismUseCase: SendDualismUseCase
) : BaseViewModel<SendDualismsEvent>() {

    fun getUserData() {
        executeUseCase(
            { getSessionUseCase.execute() },
            { result -> _event.value = result?.let { SendDualismsEvent.UserData(it) } },
            { _event.value = SendDualismsEvent.SWW }
        )
    }

    fun sendDualism(
        explanation: String?,
        txtTop: String,
        txtBottom: String,
        creatorId: String,
        creatorName: String
    ) {
        executeUseCase(
            {
                sendDualismUseCase.execute(
                    SendDualismUseCase.Params(
                        explanation,
                        txtTop,
                        txtBottom,
                        creatorId,
                        creatorName
                    )
                )
            },
            { _event.value = SendDualismsEvent.DataSent },
            { _event.value = SendDualismsEvent.SWW }
        )
    }
}