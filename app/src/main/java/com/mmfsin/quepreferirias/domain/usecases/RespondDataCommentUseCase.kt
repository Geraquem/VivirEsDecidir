package com.mmfsin.quepreferirias.domain.usecases

import com.mmfsin.quepreferirias.base.BaseUseCase
import com.mmfsin.quepreferirias.data.models.CommentDTO
import com.mmfsin.quepreferirias.data.models.CommentReplyDTO
import com.mmfsin.quepreferirias.domain.interfaces.ICommentsRepository
import com.mmfsin.quepreferirias.domain.models.Comment
import com.mmfsin.quepreferirias.domain.models.CommentReply
import com.mmfsin.quepreferirias.domain.models.Session
import com.mmfsin.quepreferirias.presentation.models.DashboardType
import com.mmfsin.quepreferirias.presentation.models.DashboardType.*
import com.mmfsin.quepreferirias.utils.DILEMMAS
import com.mmfsin.quepreferirias.utils.DUALISMS
import java.time.LocalDate
import java.util.UUID
import javax.inject.Inject

class RespondDataCommentUseCase @Inject constructor(
    private val repository: ICommentsRepository
) : BaseUseCase<RespondDataCommentUseCase.Params, CommentReply?>() {

    override suspend fun execute(params: Params): CommentReply? {
        val reply = CommentReplyDTO(
            replyId = UUID.randomUUID().toString(),
            userId = params.session.id,
            userName = params.session.name,
            image = params.session.imageUrl,
            reply = params.reply,
        )
        val root = when (params.type) {
            DILEMMA -> DILEMMAS
            DUALISM -> DUALISMS
        }
        return repository.respondComment(params.dataId, root,params.commentId, reply)
    }

    data class Params(
        val dataId: String,
        val commentId: String,
        val type: DashboardType,
        val session: Session,
        val reply: String
    )
}