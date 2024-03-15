package com.mmfsin.quepreferirias.presentation.saved

import com.mmfsin.quepreferirias.domain.models.DilemmaFav

sealed class SavedDataEvent {
    class InitiatedSession(val initiatedSession: Boolean) : SavedDataEvent()
    class Data(val data: List<DilemmaFav>) : SavedDataEvent()
    object SWW : SavedDataEvent()
}