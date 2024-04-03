package com.mmfsin.quepreferirias.data.mappers

import com.mmfsin.quepreferirias.data.models.DataDTO
import com.mmfsin.quepreferirias.domain.models.Data

fun DataDTO.toData(id: String, votesYes: Long, votesNo: Long) = Data(
    id = id,
    topText = txtTop,
    bottomText = txtBottom,
    votesYes = votesYes,
    votesNo = votesNo,
    creatorName = creatorName
)