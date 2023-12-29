package com.mmfsin.quepreferirias.data.mappers

import com.mmfsin.quepreferirias.data.models.DataDTO
import com.mmfsin.quepreferirias.data.models.SavedDataDTO
import com.mmfsin.quepreferirias.data.models.SessionDTO
import com.mmfsin.quepreferirias.domain.models.Data
import com.mmfsin.quepreferirias.domain.models.SavedData
import com.mmfsin.quepreferirias.domain.models.Session

fun DataDTO.toData(id: String, votesYes: Long, votesNo: Long) = Data(
    id = id,
    topText = txtTop,
    bottomText = txtBottom,
    votesYes = votesYes,
    votesNo = votesNo,
    creatorName = creatorName
)

fun SessionDTO.toSession() = Session(id, email, name)

fun SavedDataDTO.toSavedData() = SavedData(
    dataId = dataId,
    txtTop = txtTop,
    txtBottom = txtBottom
)

fun List<SavedDataDTO>.toSavedDataList() = this.map { element -> element.toSavedData() }.toList()
