package com.mmfsin.quepreferirias.data.mappers

import com.mmfsin.quepreferirias.data.models.CommentDTO
import com.mmfsin.quepreferirias.data.models.DilemmaDTO
import com.mmfsin.quepreferirias.data.models.SavedDataDTO
import com.mmfsin.quepreferirias.data.models.SessionDTO
import com.mmfsin.quepreferirias.domain.models.Comment
import com.mmfsin.quepreferirias.domain.models.Dilemma
import com.mmfsin.quepreferirias.domain.models.SavedData
import com.mmfsin.quepreferirias.domain.models.Session

fun DilemmaDTO.toDilemma(id: String, votesYes: Long, votesNo: Long) = Dilemma(
    id = id,
    topText = txtTop,
    bottomText = txtBottom,
    votesYes = votesYes,
    votesNo = votesNo,
    creatorName = creatorName
)

fun SessionDTO.toSession() = Session(
    id = id,
    imageUrl = imageUrl,
    email = email,
    name = name,
    fullName = fullName
)

fun SavedDataDTO.toSavedData() = SavedData(
    dataId = dataId,
    txtTop = txtTop,
    txtBottom = txtBottom
)

fun List<SavedDataDTO>.toSavedDataList() = this.map { element -> element.toSavedData() }.toList()

fun CommentDTO.toComment() = Comment(
    name = name,
    comment = comment,
    image = image,
    date = date,
    likes = likes
)

fun List<CommentDTO>.toCommentList() = this.map { comment -> comment.toComment() }.toList()
