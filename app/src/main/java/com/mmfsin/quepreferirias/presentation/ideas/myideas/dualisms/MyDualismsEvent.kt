package com.mmfsin.quepreferirias.presentation.ideas.myideas.dualisms

import com.mmfsin.quepreferirias.domain.models.SendDualism

sealed class MyDualismsEvent {
    class Dualisms(val data: List<SendDualism>) : MyDualismsEvent()
    object Deleted : MyDualismsEvent()
    object SWW : MyDualismsEvent()
}