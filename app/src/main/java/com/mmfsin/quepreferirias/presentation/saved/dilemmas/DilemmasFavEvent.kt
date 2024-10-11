package com.mmfsin.quepreferirias.presentation.saved.dilemmas

import com.mmfsin.quepreferirias.domain.models.DilemmaFav

sealed class DilemmasFavEvent {
    class Data(val data: List<DilemmaFav>) : DilemmasFavEvent()
    object FavDeleted : DilemmasFavEvent()
    object SWW : DilemmasFavEvent()
}