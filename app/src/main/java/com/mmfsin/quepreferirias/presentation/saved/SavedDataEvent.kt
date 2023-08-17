package com.mmfsin.quepreferirias.presentation.saved

import com.mmfsin.quepreferirias.domain.models.SavedData

sealed class SavedDataEvent {
    class Data(val data: List<SavedData>) : SavedDataEvent()
    object SWW : SavedDataEvent()
}