package com.mmfsin.quepreferirias.presentation.myideas.dilemmas

import com.mmfsin.quepreferirias.domain.models.DilemmaFav

sealed class MyDilemmasEvent {
    class InitiatedSession(val initiatedSession: Boolean) : MyDilemmasEvent()
    class Data(val data: List<DilemmaFav>) : MyDilemmasEvent()
    object SWW : MyDilemmasEvent()
}