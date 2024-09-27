package com.mmfsin.quepreferirias.data.mappers

import com.mmfsin.quepreferirias.data.models.CommentDTO
import com.mmfsin.quepreferirias.data.models.DilemmaFavDTO
import com.mmfsin.quepreferirias.data.models.DilemmaVotedDTO
import com.mmfsin.quepreferirias.data.models.SendDilemmaDTO
import com.mmfsin.quepreferirias.data.models.SessionDTO
import com.mmfsin.quepreferirias.domain.models.DilemmaFav
import com.mmfsin.quepreferirias.domain.models.DilemmaVoted
import com.mmfsin.quepreferirias.domain.models.SendDilemma
import com.mmfsin.quepreferirias.domain.models.Session
import java.time.LocalDate
import java.util.UUID

fun Session.toSessionDTO() = SessionDTO(
    id = id,
    imageUrl = imageUrl,
    email = email,
    name = name,
    fullName = fullName,
    instagram = rrss?.instagram,
    twitter = rrss?.twitter,
    tiktok = rrss?.tiktok,
    youtube = rrss?.youtube,
)

fun DilemmaFav.toDilemmaFavDTO() = DilemmaFavDTO(
    dilemmaId = dilemmaId,
    txtTop = txtTop,
    txtBottom = txtBottom
)

fun DilemmaVoted.toDilemmaVotedDTO() = DilemmaVotedDTO(
    dilemmaId = dilemmaId,
    votedYes = votedYes
)

fun SendDilemma.toSendDilemmaDTO() = SendDilemmaDTO(
    dilemmaId = dilemmaId,
    txtTop = txtTop,
    txtBottom = txtBottom,
    creatorId = creatorId,
    creatorName = creatorName,
    timestamp = timestamp,
    filterValue = filterValue
)