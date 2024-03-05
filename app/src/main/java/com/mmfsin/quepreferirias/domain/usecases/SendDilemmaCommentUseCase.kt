package com.mmfsin.quepreferirias.domain.usecases

import com.mmfsin.quepreferirias.base.BaseUseCase
import com.mmfsin.quepreferirias.domain.interfaces.IDataRepository
import com.mmfsin.quepreferirias.domain.interfaces.IQuestionsRepository
import com.mmfsin.quepreferirias.domain.models.Comment
import com.mmfsin.quepreferirias.domain.models.Session
import javax.inject.Inject

class SendDilemmaCommentUseCase @Inject constructor(private val repository: IDataRepository) :
    BaseUseCase<SendDilemmaCommentUseCase.Params, Boolean>() {

    override suspend fun execute(params: Params): Boolean {
        val comment = Comment(
            userId = params.session.id,
            name = params.session.name,
            comment = params.comment,
            image = params.session.imageUrl,
            date = System.currentTimeMillis().toString(),
            likes = 0
        )
        return repository.setDilemmaComment(params.dilemmaId, comment)
    }

    data class Params(
        val dilemmaId: String,
        val session: Session,
        val comment: String
    )
}