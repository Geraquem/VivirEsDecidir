package com.mmfsin.quepreferirias.presentation.saved.dilemmas

import com.mmfsin.quepreferirias.domain.models.DilemmaFav

sealed class DilemmaFavEvent {
    class Data(val data: List<DilemmaFav>) : DilemmaFavEvent()
    object SWW : DilemmaFavEvent()
}