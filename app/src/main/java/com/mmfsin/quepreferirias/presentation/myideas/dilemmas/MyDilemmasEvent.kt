package com.mmfsin.quepreferirias.presentation.myideas.dilemmas

import com.mmfsin.quepreferirias.domain.models.SendDilemma

sealed class MyDilemmasEvent {
    class Data(val data: List<SendDilemma>) : MyDilemmasEvent()
    object SWW : MyDilemmasEvent()
}