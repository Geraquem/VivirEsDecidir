package com.mmfsin.quepreferirias.presentation.dashboard.dilemmas

import com.mmfsin.quepreferirias.domain.models.Comment
import com.mmfsin.quepreferirias.domain.models.CommentVote
import com.mmfsin.quepreferirias.domain.models.Dilemma
import com.mmfsin.quepreferirias.presentation.models.Percents

sealed class DilemmasEvent {
    class Data(val data: List<Dilemma>) : DilemmasEvent()
    class GetPercents(val percents: Percents) : DilemmasEvent()
    class GetComments(val comments: List<Comment>) : DilemmasEvent()
    object SWW : DilemmasEvent()
}