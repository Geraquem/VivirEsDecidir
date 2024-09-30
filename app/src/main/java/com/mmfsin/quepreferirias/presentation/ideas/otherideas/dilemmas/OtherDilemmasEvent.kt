package com.mmfsin.quepreferirias.presentation.ideas.otherideas.dilemmas

import com.mmfsin.quepreferirias.domain.models.SendDilemma

sealed class OtherDilemmasEvent {
    class Dilemmas(val data: List<SendDilemma>) : OtherDilemmasEvent()
    object SWW : OtherDilemmasEvent()
}