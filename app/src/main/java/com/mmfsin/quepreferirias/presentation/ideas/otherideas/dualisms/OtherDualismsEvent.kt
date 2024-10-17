package com.mmfsin.quepreferirias.presentation.ideas.otherideas.dualisms

import com.mmfsin.quepreferirias.domain.models.SendDualism

sealed class OtherDualismsEvent {
    class Dualisms(val data: List<SendDualism>) : OtherDualismsEvent()
    object SWW : OtherDualismsEvent()
}