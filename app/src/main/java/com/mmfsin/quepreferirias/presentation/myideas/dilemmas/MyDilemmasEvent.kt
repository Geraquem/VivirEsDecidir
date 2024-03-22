package com.mmfsin.quepreferirias.presentation.myideas.dilemmas

import com.mmfsin.quepreferirias.domain.models.SendDilemma
import com.mmfsin.quepreferirias.presentation.saved.dilemmas.DilemmaFavEvent

sealed class MyDilemmasEvent {
    class Data(val data: List<SendDilemma>) : MyDilemmasEvent()
    object Deleted : MyDilemmasEvent()
    object SWW : MyDilemmasEvent()
}