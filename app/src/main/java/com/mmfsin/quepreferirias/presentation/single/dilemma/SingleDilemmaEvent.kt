package com.mmfsin.quepreferirias.presentation.single.dilemma

import com.mmfsin.quepreferirias.domain.models.Comment
import com.mmfsin.quepreferirias.domain.models.Dilemma
import com.mmfsin.quepreferirias.presentation.models.Percents

sealed class SingleDilemmaEvent {
    class InitiatedSession(val initiatedSession: Boolean) : SingleDilemmaEvent()
    class ReCheckSession(val initiatedSession: Boolean) : SingleDilemmaEvent()
    class GetDilemma(val data: Dilemma) : SingleDilemmaEvent()
    class GetPercents(val percents: Percents) : SingleDilemmaEvent()
    class GetComments(val comments: List<Comment>) : SingleDilemmaEvent()
    class CheckDilemmaFav(val result: Boolean) : SingleDilemmaEvent()
    object SWW : SingleDilemmaEvent()
}