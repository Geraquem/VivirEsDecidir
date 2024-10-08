package com.mmfsin.quepreferirias.presentation.dashboard.dualisms

import com.mmfsin.quepreferirias.domain.models.Dualism
import com.mmfsin.quepreferirias.domain.models.DualismVotes
import com.mmfsin.quepreferirias.presentation.models.Percents

sealed class DualismsEvent {
    class InitiatedSession(val initiatedSession: Boolean) : DualismsEvent()
    class ReCheckSession(val initiatedSession: Boolean) : DualismsEvent()
    class NavigateToProfile(val isMe: Boolean, val userId: String) : DualismsEvent()

    class Dualisms(val data: List<Dualism>) : DualismsEvent()
    class CheckDualismFav(val result: Boolean) : DualismsEvent()
    class GetVotes(val votes: DualismVotes) : DualismsEvent()
    class GetPercents(val percents: Percents) : DualismsEvent()

    class VoteDilemma(val wasTop: Boolean) : DualismsEvent()

    object SWW : DualismsEvent()
}