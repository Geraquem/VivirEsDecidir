package com.mmfsin.quepreferirias.presentation.sendquestions

import com.mmfsin.quepreferirias.domain.models.Data
import com.mmfsin.quepreferirias.presentation.models.Percents

sealed class SendQuestionsEvent {
    class AppData(val data: List<Data>) : SendQuestionsEvent()
    class GetPercents(val percents: Percents) : SendQuestionsEvent()
    object SWW : SendQuestionsEvent()
}