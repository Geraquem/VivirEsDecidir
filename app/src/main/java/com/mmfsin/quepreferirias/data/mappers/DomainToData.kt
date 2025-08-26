package com.mmfsin.quepreferirias.data.mappers

import com.mmfsin.quepreferirias.data.models.DilemmaFavDTO
import com.mmfsin.quepreferirias.data.models.DilemmaVotedDTO
import com.mmfsin.quepreferirias.data.models.DualismFavDTO
import com.mmfsin.quepreferirias.data.models.DualismVotedDTO
import com.mmfsin.quepreferirias.data.models.SendDilemmaDTO
import com.mmfsin.quepreferirias.data.models.SendDualismDTO
import com.mmfsin.quepreferirias.data.models.SessionDTO
import com.mmfsin.quepreferirias.domain.models.DilemmaFav
import com.mmfsin.quepreferirias.domain.models.DilemmaVoted
import com.mmfsin.quepreferirias.domain.models.DualismFav
import com.mmfsin.quepreferirias.domain.models.DualismVoted
import com.mmfsin.quepreferirias.domain.models.SendDilemma
import com.mmfsin.quepreferirias.domain.models.SendDualism
import com.mmfsin.quepreferirias.domain.models.Session

fun toSessionDTO(session: Session) = SessionDTO().apply {
    id = session.id
    imageUrl = session.imageUrl
    email = session.email
    name = session.name
    fullName = session.fullName
    instagram = session.rrss?.instagram
    twitter = session.rrss?.twitter
    tiktok = session.rrss?.tiktok
    youtube = session.rrss?.youtube
}

fun toDilemmaFavDTO(dilemma: DilemmaFav) = DilemmaFavDTO().apply {
    dilemmaId = dilemma.dilemmaId
    txtTop = dilemma.txtTop
    txtBottom = dilemma.txtBottom
}

fun toDualismFavDTO(dualismFav: DualismFav) = DualismFavDTO().apply {
    dualismId = dualismFav.dualismId
    txtTop = dualismFav.txtTop
    txtBottom = dualismFav.txtBottom
}

fun toDilemmaVotedDTO(dilemmaVoted: DilemmaVoted) = DilemmaVotedDTO().apply {
    dilemmaId = dilemmaVoted.dilemmaId
    votedYes = dilemmaVoted.votedYes
}

fun toDualismVotedDTO(dualismVoted: DualismVoted) = DualismVotedDTO().apply {
    dualismId = dualismVoted.dualismId
    votedTop = dualismVoted.votedTop
}

fun toSendDilemmaDTO(a: SendDilemma) = SendDilemmaDTO().apply {
    dilemmaId = a.dilemmaId
    txtTop = a.txtTop
    txtBottom = a.txtBottom
    creatorId = a.creatorId
    creatorName = a.creatorName
    timestamp = a.timestamp
    filterValue = a.filterValue
}

fun toSendDualismDTO(sendDualism: SendDualism) = SendDualismDTO().apply {
    dualismId = sendDualism.dualismId
    explanation = sendDualism.explanation
    txtTop = sendDualism.txtTop
    txtBottom = sendDualism.txtBottom
    creatorId = sendDualism.creatorId
    creatorName = sendDualism.creatorName
    timestamp = sendDualism.timestamp
    filterValue = sendDualism.filterValue
}