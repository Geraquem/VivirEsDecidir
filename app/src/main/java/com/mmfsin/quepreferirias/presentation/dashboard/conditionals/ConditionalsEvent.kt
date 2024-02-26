package com.mmfsin.quepreferirias.presentation.dashboard.conditionals

import com.mmfsin.quepreferirias.domain.models.ConditionalData
import com.mmfsin.quepreferirias.presentation.models.Percents

sealed class ConditionalsEvent {
    class Data(val data: List<ConditionalData>) : ConditionalsEvent()
    class GetPercents(val percents: Percents) : ConditionalsEvent()
    object SWW : ConditionalsEvent()
}