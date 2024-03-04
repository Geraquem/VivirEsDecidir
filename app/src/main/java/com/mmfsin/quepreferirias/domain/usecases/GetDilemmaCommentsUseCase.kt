package com.mmfsin.quepreferirias.domain.usecases

import com.mmfsin.quepreferirias.base.BaseUseCase
import com.mmfsin.quepreferirias.domain.interfaces.IDataRepository
import com.mmfsin.quepreferirias.domain.models.Comment
import javax.inject.Inject

class GetDilemmaCommentsUseCase @Inject constructor(val repository: IDataRepository) :
    BaseUseCase<GetDilemmaCommentsUseCase.Params, List<Comment>>() {

    override suspend fun execute(params: Params): List<Comment> =
        repository.getDilemmaComments(params.dilemmaId)

    data class Params(
        val dilemmaId: String
    )
}