package com.mmfsin.quepreferirias.presentation.saved.dilemmas

import com.mmfsin.quepreferirias.domain.models.DilemmaFav

sealed class DilemmaFavEvent {
    class InitiatedSession(val initiatedSession: Boolean) : DilemmaFavEvent()
    class Data(val data: List<DilemmaFav>) : DilemmaFavEvent()
    object SWW : DilemmaFavEvent()
}