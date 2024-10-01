package com.mmfsin.quepreferirias.presentation.send.dualisms

import com.mmfsin.quepreferirias.domain.models.Session

sealed class SendDualismsEvent {
    class UserData(val session: Session) : SendDualismsEvent()
    object DataSent : SendDualismsEvent()
    object SWW : SendDualismsEvent()
}