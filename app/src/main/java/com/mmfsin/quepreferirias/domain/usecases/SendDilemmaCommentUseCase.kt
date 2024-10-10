package com.mmfsin.quepreferirias.domain.usecases

import com.mmfsin.quepreferirias.base.BaseUseCase
import com.mmfsin.quepreferirias.data.models.CommentDTO
import com.mmfsin.quepreferirias.domain.interfaces.ICommentsRepository
import com.mmfsin.quepreferirias.domain.models.Comment
import com.mmfsin.quepreferirias.domain.models.Session
import com.mmfsin.quepreferirias.presentation.models.DashboardType
import com.mmfsin.quepreferirias.presentation.models.DashboardType.*
import com.mmfsin.quepreferirias.utils.DILEMMAS
import com.mmfsin.quepreferirias.utils.DUALISMS
import java.time.LocalDate
import java.util.UUID
import javax.inject.Inject

class SendDilemmaCommentUseCase @Inject constructor(
    private val repository: ICommentsRepository
) : BaseUseCase<SendDilemmaCommentUseCase.Params, Comment?>() {

    override suspend fun execute(params: Params): Comment? {
        val comment = CommentDTO(
            commentId = UUID.randomUUID().toString(),
            userId = params.session.id,
            name = params.session.name,
            comment = params.comment,
            image = params.session.imageUrl,
            timestamp = System.currentTimeMillis(),
            date = LocalDate.now().toString()
        )
        val root = when (params.type) {
            DILEMMA -> DILEMMAS
            DUALISM -> DUALISMS
        }
        return repository.sendDilemmaComment(params.dataId, root, comment)
    }

    data class Params(
        val dataId: String,
        val type: DashboardType,
        val session: Session,
        val comment: String
    )
}