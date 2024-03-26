package com.mmfsin.quepreferirias.data.mappers

import com.mmfsin.quepreferirias.data.models.DilemmaFavDTO
import com.mmfsin.quepreferirias.data.models.RRSSDTO
import com.mmfsin.quepreferirias.data.models.SendDilemmaDTO
import com.mmfsin.quepreferirias.data.models.SessionDTO
import com.mmfsin.quepreferirias.domain.models.DilemmaFav
import com.mmfsin.quepreferirias.domain.models.RRSS
import com.mmfsin.quepreferirias.domain.models.SendDilemma
import com.mmfsin.quepreferirias.domain.models.Session

fun Session.toSessionDTO() = SessionDTO(
    id = id,
    imageUrl = imageUrl,
    email = email,
    name = name,
    fullName = fullName,
    rrss = rrss?.toRrssDTO()
)

fun RRSS.toRrssDTO() = RRSSDTO(
    instagram = instagram,
    twitter = twitter,
    tiktok = tiktok,
    youtube = youtube
)

fun DilemmaFav.toDilemmaFavDTO() = DilemmaFavDTO(
    dilemmaId = dilemmaId,
    txtTop = txtTop,
    txtBottom = txtBottom
)

fun SendDilemma.toSendDilemmaDTO() = SendDilemmaDTO(
    dilemmaId = dilemmaId,
    txtTop = txtTop,
    txtBottom = txtBottom,
    creatorId = creatorId,
    creatorName = creatorName,
    timestamp = timestamp
)