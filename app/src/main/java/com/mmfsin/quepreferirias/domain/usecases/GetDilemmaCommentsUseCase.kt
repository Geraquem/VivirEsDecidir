package com.mmfsin.quepreferirias.domain.usecases

import com.mmfsin.quepreferirias.base.BaseUseCase
import com.mmfsin.quepreferirias.domain.interfaces.ICommentsRepository
import com.mmfsin.quepreferirias.domain.models.Comment
import javax.inject.Inject

class GetDilemmaCommentsUseCase @Inject constructor(
    val repository: ICommentsRepository
) : BaseUseCase<GetDilemmaCommentsUseCase.Params, List<Comment>>() {

    override suspend fun execute(params: Params): List<Comment> {
        return repository.getDilemmaComments(params.dilemmaId)
    }

    data class Params(
        val dilemmaId: String,
    )
}