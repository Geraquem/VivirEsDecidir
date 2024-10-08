package com.mmfsin.quepreferirias.domain.usecases

import com.mmfsin.quepreferirias.base.BaseUseCase
import com.mmfsin.quepreferirias.domain.interfaces.ICommentsRepository
import javax.inject.Inject

class DeleteDilemmaCommentUseCase @Inject constructor(
    private val repository: ICommentsRepository
) : BaseUseCase<DeleteDilemmaCommentUseCase.Params, Boolean>() {

    override suspend fun execute(params: Params) =
        repository.deleteDilemmaComment(params.dilemmaId, params.commentId)

    data class Params(
        val dilemmaId: String,
        val commentId: String,
    )
}