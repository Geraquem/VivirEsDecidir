package com.mmfsin.quepreferirias.presentation.send.dilemmas

import com.mmfsin.quepreferirias.domain.models.Session

sealed class SendDilemmaEvent {
    class UserData(val session: Session) : SendDilemmaEvent()
    class Result(val result: Boolean) : SendDilemmaEvent()
    object SWW : SendDilemmaEvent()
}