package com.mmfsin.quepreferirias.presentation.initial

import com.mmfsin.quepreferirias.domain.models.Session

sealed class InitialEvent {
    class InitiatedSession(val initiatedSession: Boolean) : InitialEvent()
    class ReCheckSession(val initiatedSession: Boolean) : InitialEvent()
    class GetSession(val session: Session?) : InitialEvent()

    object SWW : InitialEvent()
}