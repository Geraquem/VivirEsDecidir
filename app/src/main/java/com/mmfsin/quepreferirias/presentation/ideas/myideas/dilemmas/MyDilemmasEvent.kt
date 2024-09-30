package com.mmfsin.quepreferirias.presentation.ideas.myideas.dilemmas

import com.mmfsin.quepreferirias.domain.models.SendDilemma

sealed class MyDilemmasEvent {
    class Dilemmas(val data: List<SendDilemma>) : MyDilemmasEvent()
    object Deleted : MyDilemmasEvent()
    object SWW : MyDilemmasEvent()
}