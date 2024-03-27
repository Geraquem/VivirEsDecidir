package com.mmfsin.quepreferirias.presentation.dashboard.dilemmas

import com.mmfsin.quepreferirias.domain.models.Comment
import com.mmfsin.quepreferirias.domain.models.Dilemma
import com.mmfsin.quepreferirias.presentation.models.Percents

sealed class DilemmasEvent {
    class InitiatedSession(val initiatedSession: Boolean) : DilemmasEvent()
    class ReCheckSession(val initiatedSession: Boolean) : DilemmasEvent()
    class NavigateToProfile(val isMe: Boolean, val userId: String) : DilemmasEvent()
    class Dilemmas(val data: List<Dilemma>) : DilemmasEvent()
    class GetPercents(val percents: Percents) : DilemmasEvent()
    class GetComments(val comments: List<Comment>) : DilemmasEvent()
    class CheckDilemmaFav(val result: Boolean) : DilemmasEvent()
    object SWW : DilemmasEvent()
}