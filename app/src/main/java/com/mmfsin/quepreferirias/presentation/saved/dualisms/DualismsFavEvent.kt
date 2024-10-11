package com.mmfsin.quepreferirias.presentation.saved.dualisms

import com.mmfsin.quepreferirias.domain.models.DualismFav

sealed class DualismsFavEvent {
    class Data(val data: List<DualismFav>) : DualismsFavEvent()
    object FavDeleted : DualismsFavEvent()
    object SWW : DualismsFavEvent()
}