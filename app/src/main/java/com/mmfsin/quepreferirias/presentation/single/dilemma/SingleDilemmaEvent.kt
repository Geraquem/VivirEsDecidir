package com.mmfsin.quepreferirias.presentation.single.dilemma

import com.mmfsin.quepreferirias.domain.models.Comment
import com.mmfsin.quepreferirias.domain.models.Dilemma
import com.mmfsin.quepreferirias.domain.models.DilemmaVotes
import com.mmfsin.quepreferirias.domain.models.Session
import com.mmfsin.quepreferirias.presentation.models.Percents

sealed class SingleDilemmaEvent {
    class InitiatedSession(val initiatedSession: Boolean) : SingleDilemmaEvent()
    class ReCheckSession(val initiatedSession: Boolean) : SingleDilemmaEvent()
    class GetSessionToComment(val session: Session?) : SingleDilemmaEvent()
    class NavigateToProfile(val isMe: Boolean, val userId: String) : SingleDilemmaEvent()

    class SingleDilemma(val data: Dilemma) : SingleDilemmaEvent()
    class CheckDilemmaFav(val result: Boolean) : SingleDilemmaEvent()
    class GetVotes(val votes: DilemmaVotes) : SingleDilemmaEvent()
    class GetPercents(val percents: Percents) : SingleDilemmaEvent()

    class VoteDilemma(val wasYes: Boolean) : SingleDilemmaEvent()
    class AlreadyVoted(val voted: Boolean?) : SingleDilemmaEvent()

    object Reported : SingleDilemmaEvent()
    object SWW : SingleDilemmaEvent()
}