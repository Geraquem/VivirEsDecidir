package com.mmfsin.quepreferirias.presentation.saved.dilemmas

import com.mmfsin.quepreferirias.domain.models.DilemmaFav

sealed class DilemmaFavDataEvent {
    class InitiatedSession(val initiatedSession: Boolean) : DilemmaFavDataEvent()
    class Data(val data: List<DilemmaFav>) : DilemmaFavDataEvent()
    object SWW : DilemmaFavDataEvent()
}