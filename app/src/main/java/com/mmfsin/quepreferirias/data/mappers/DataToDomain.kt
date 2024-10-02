package com.mmfsin.quepreferirias.data.mappers

import com.mmfsin.quepreferirias.R
import com.mmfsin.quepreferirias.data.models.CommentDTO
import com.mmfsin.quepreferirias.data.models.DilemmaDTO
import com.mmfsin.quepreferirias.data.models.DilemmaFavDTO
import com.mmfsin.quepreferirias.data.models.SendDilemmaDTO
import com.mmfsin.quepreferirias.data.models.SessionDTO
import com.mmfsin.quepreferirias.domain.models.Comment
import com.mmfsin.quepreferirias.domain.models.Dilemma
import com.mmfsin.quepreferirias.domain.models.DilemmaFav
import com.mmfsin.quepreferirias.domain.models.RRSS
import com.mmfsin.quepreferirias.domain.models.SendDilemma
import com.mmfsin.quepreferirias.domain.models.Session
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

fun SessionDTO.toSession() = Session(
    id = id,
    imageUrl = imageUrl,
    email = email,
    name = name,
    fullName = fullName,
    rrss = RRSS(instagram, twitter, tiktok, youtube)
)

fun CommentDTO.toComment() = Comment(
    commentId = commentId,
    userId = userId,
    name = name,
    comment = comment,
    image = image,
    since = getDateTime(date),
    likes = likes,
    votedUp = votedUp,
    votedDown = votedDown
)

fun List<CommentDTO>.toCommentList() = this.map { comment -> comment.toComment() }.toList()

private fun getDateTime(date: String): Int {
    val text = try {
        val now = LocalDate.now()
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val commentDate = LocalDate.parse(date, dateFormatter)
        when (ChronoUnit.DAYS.between(commentDate, now)) {
            0L -> R.string.comments_today
            1L -> R.string.comments_yesterday
            2L -> R.string.comments_two_days
            in 3L..6L -> R.string.comments_few_days
            in 7L..13L -> R.string.comments_one_week
            in 14L..20L -> R.string.comments_two_week
            in 21L..27L -> R.string.comments_three_week
            in 28L..59L -> R.string.comments_one_month
            in 60L..90L -> R.string.comments_two_month
            in 90..120L -> R.string.comments_three_month
            else -> R.string.comments_more_than_three_month
        }
    } catch (e: Exception) {
        R.string.comments_error
    }
    return text
}

fun DilemmaDTO.toDilemma() = Dilemma(
    id = dilemmaId,
    txtTop = txtTop,
    txtBottom = txtBottom,
    creatorId = creatorId,
    creatorName = creatorName
)

fun DilemmaFavDTO.toDilemmaFav() = DilemmaFav(
    dilemmaId = dilemmaId,
    txtTop = txtTop,
    txtBottom = txtBottom
)

fun List<DilemmaFavDTO>.toDilemmaFavList() = this.map { dilemma -> dilemma.toDilemmaFav() }.toList()

fun SendDilemmaDTO.toSendDilemma() = SendDilemma(
    dilemmaId = dilemmaId,
    txtTop = txtTop,
    txtBottom = txtBottom,
    creatorName = creatorName,
    timestamp = timestamp,
    creatorId = creatorId,
    filterValue = filterValue

)

fun List<SendDilemmaDTO>.toSendDilemmaList() =
    this.map { dilemma -> dilemma.toSendDilemma() }.toList()