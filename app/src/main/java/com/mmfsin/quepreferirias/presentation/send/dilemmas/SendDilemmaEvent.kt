package com.mmfsin.quepreferirias.presentation.send.dilemmas

import com.mmfsin.quepreferirias.domain.models.Session

sealed class SendDilemmaEvent {
    class UserData(val session: Session) : SendDilemmaEvent()
    object DataSent : SendDilemmaEvent()
    object SWW : SendDilemmaEvent()
}