package com.mmfsin.quepreferirias.presentation.single.dualism

import com.mmfsin.quepreferirias.domain.models.Dualism
import com.mmfsin.quepreferirias.domain.models.DualismVotes
import com.mmfsin.quepreferirias.domain.models.Session
import com.mmfsin.quepreferirias.presentation.models.Percents

sealed class SingleDualismEvent {
    class InitiatedSession(val initiatedSession: Boolean) : SingleDualismEvent()
    class ReCheckSession(val initiatedSession: Boolean) : SingleDualismEvent()
    class GetSessionToComment(val session: Session?) : SingleDualismEvent()
    class NavigateToProfile(val isMe: Boolean, val userId: String) : SingleDualismEvent()

    class SingleDualism(val data: Dualism) : SingleDualismEvent()
    class CheckDualismFav(val result: Boolean) : SingleDualismEvent()
    class GetVotes(val votes: DualismVotes) : SingleDualismEvent()
    class GetPercents(val percents: Percents) : SingleDualismEvent()

    class VoteDualism(val wasTop: Boolean) : SingleDualismEvent()
    class AlreadyVoted(val voted: Boolean?) : SingleDualismEvent()

    object Reported : SingleDualismEvent()
    object SWW : SingleDualismEvent()
}