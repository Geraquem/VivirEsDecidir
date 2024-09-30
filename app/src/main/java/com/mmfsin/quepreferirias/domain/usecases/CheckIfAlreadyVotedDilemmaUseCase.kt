package com.mmfsin.quepreferirias.domain.usecases

import com.mmfsin.quepreferirias.base.BaseUseCase
import com.mmfsin.quepreferirias.domain.interfaces.IDilemmasRepository
import com.mmfsin.quepreferirias.domain.models.CommentVote
import javax.inject.Inject

class CheckIfAlreadyVotedDilemmaUseCase @Inject constructor(
    private val repository: IDilemmasRepository
) : BaseUseCase<CheckIfAlreadyVotedDilemmaUseCase.Params, Boolean?>() {

    override suspend fun execute(params: Params): Boolean? =
        repository.alreadyDilemmaVoted(params.dilemmaId)

    data class Params(
        val dilemmaId: String,
    )
}