package com.mmfsin.quepreferirias.data.mappers

import com.mmfsin.quepreferirias.data.models.DataDTO
import com.mmfsin.quepreferirias.data.models.SessionDTO
import com.mmfsin.quepreferirias.domain.models.Data
import com.mmfsin.quepreferirias.domain.models.Session

fun DataDTO.toData(id: String, votesYes: Long, votesNo: Long) = Data(
    id = id,
    topText = txtA,
    bottomText = txtB,
    votesYes = votesYes,
    votesNo = votesNo,
    creatorName = creatorName
)

fun SessionDTO.toSession() = Session(id, name, initiated)