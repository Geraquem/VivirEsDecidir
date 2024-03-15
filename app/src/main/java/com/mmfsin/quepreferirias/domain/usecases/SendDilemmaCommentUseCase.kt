package com.mmfsin.quepreferirias.domain.usecases

import com.mmfsin.quepreferirias.base.BaseUseCase
import com.mmfsin.quepreferirias.data.models.CommentDTO
import com.mmfsin.quepreferirias.domain.interfaces.IDilemmasRepository
import com.mmfsin.quepreferirias.domain.models.Session
import java.time.LocalDate
import java.util.UUID
import javax.inject.Inject

class SendDilemmaCommentUseCase @Inject constructor(private val repository: IDilemmasRepository) :
    BaseUseCase<SendDilemmaCommentUseCase.Params, Boolean>() {

    override suspend fun execute(params: Params): Boolean {
        val comment = CommentDTO(
            commentId = UUID.randomUUID().toString(),
            userId = params.session.id,
            name = params.session.name,
            comment = params.comment,
            image = params.session.imageUrl,
            timestamp = System.currentTimeMillis().toString(),
            date = LocalDate.now().toString()
        )
        return repository.sendDilemmaComment(params.dilemmaId, comment)
    }

    data class Params(
        val dilemmaId: String,
        val session: Session,
        val comment: String
    )
}