package com.mmfsin.quepreferirias.presentation.dashboard.conditionals

import com.mmfsin.quepreferirias.domain.models.ConditionalData
import com.mmfsin.quepreferirias.presentation.models.Percents

sealed class ConditionalsEvent {
    class Data(val data: List<ConditionalData>) : ConditionalsEvent()
    class GetPercents(val percents: Percents) : ConditionalsEvent()
    class AlreadySaved(val saved: Boolean?) : ConditionalsEvent()
    class DataSaved(val result: Boolean?) : ConditionalsEvent()
    object SWW : ConditionalsEvent()
}