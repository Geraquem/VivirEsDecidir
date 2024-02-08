package com.mmfsin.quepreferirias.presentation.initial

import com.mmfsin.quepreferirias.domain.models.Data
import com.mmfsin.quepreferirias.presentation.models.Percents

sealed class InitialEvent {
    object SWW : InitialEvent()
}