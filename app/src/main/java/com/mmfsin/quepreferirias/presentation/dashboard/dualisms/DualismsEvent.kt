package com.mmfsin.quepreferirias.presentation.dashboard.dualisms

sealed class DualismsEvent {
    class InitiatedSession(val initiatedSession: Boolean) : DualismsEvent()
    class ReCheckSession(val initiatedSession: Boolean) : DualismsEvent()

    object SWW : DualismsEvent()
}