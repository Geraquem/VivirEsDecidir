package com.mmfsin.quepreferirias.presentation.dashboard.dilemmas

import com.mmfsin.quepreferirias.domain.models.Dilemma
import com.mmfsin.quepreferirias.domain.models.DilemmaVotes
import com.mmfsin.quepreferirias.domain.models.Session
import com.mmfsin.quepreferirias.presentation.models.Percents

sealed class DilemmasEvent {
    class InitiatedSession(val initiatedSession: Boolean) : DilemmasEvent()
    class ReCheckSession(val initiatedSession: Boolean) : DilemmasEvent()
    class GetSessionToComment(val session: Session?) : DilemmasEvent()
    class NavigateToProfile(val isMe: Boolean, val userId: String) : DilemmasEvent()

    class Dilemmas(val data: List<Dilemma>) : DilemmasEvent()
    class CheckDilemmaFav(val result: Boolean) : DilemmasEvent()
    class GetVotes(val votes: DilemmaVotes) : DilemmasEvent()
    class GetPercents(val percents: Percents) : DilemmasEvent()

    class VoteDilemma(val wasYes: Boolean) : DilemmasEvent()

    object Reported : DilemmasEvent()
    object SWW : DilemmasEvent()
}