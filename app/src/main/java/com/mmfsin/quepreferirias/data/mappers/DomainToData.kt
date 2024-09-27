package com.mmfsin.quepreferirias.data.mappers

import com.mmfsin.quepreferirias.data.models.DilemmaFavDTO
import com.mmfsin.quepreferirias.data.models.SendDilemmaDTO
import com.mmfsin.quepreferirias.data.models.SessionDTO
import com.mmfsin.quepreferirias.domain.models.DilemmaFav
import com.mmfsin.quepreferirias.domain.models.SendDilemma
import com.mmfsin.quepreferirias.domain.models.Session

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

fun SendDilemma.toSendDilemmaDTO() = SendDilemmaDTO(
    dilemmaId = dilemmaId,
    txtTop = txtTop,
    txtBottom = txtBottom,
    creatorId = creatorId,
    creatorName = creatorName,
    timestamp = timestamp,
    filterValue = filterValue
)