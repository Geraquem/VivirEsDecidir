package com.mmfsin.quepreferirias.presentation.send.dilemmas

import com.mmfsin.quepreferirias.domain.models.Session

sealed class SendDilemmaEvent {
    class UserData(val session: Session) : SendDilemmaEvent()
    object Result : SendDilemmaEvent()
    object SWW : SendDilemmaEvent()
}