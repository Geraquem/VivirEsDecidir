package com.mmfsin.quepreferirias.presentation.saved

import com.mmfsin.quepreferirias.domain.models.Dilemma

sealed class SavedDataEvent {
    class InitiatedSession(val initiatedSession: Boolean) : SavedDataEvent()
    class Data(val data: List<Dilemma>) : SavedDataEvent()
    object SWW : SavedDataEvent()
}