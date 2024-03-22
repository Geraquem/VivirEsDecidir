package com.mmfsin.quepreferirias.domain.usecases

import com.mmfsin.quepreferirias.base.BaseUseCase
import com.mmfsin.quepreferirias.domain.interfaces.IDilemmasRepository
import com.mmfsin.quepreferirias.domain.models.Comment
import javax.inject.Inject

class GetDilemmaCommentsUseCase @Inject constructor(
    val repository: IDilemmasRepository
) : BaseUseCase<GetDilemmaCommentsUseCase.Params, List<Comment>>() {

    override suspend fun execute(params: Params): List<Comment> {
        return if (params.fromRealm) repository.getDilemmaCommentsFromRealm()
        else params.dilemmaId?.let { repository.getDilemmaComments(params.dilemmaId) }
            ?: run { emptyList() }
    }

    data class Params(
        val dilemmaId: String? = null,
        val fromRealm: Boolean = false
    )
}