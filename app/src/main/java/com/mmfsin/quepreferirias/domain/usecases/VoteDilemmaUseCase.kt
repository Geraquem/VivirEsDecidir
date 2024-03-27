package com.mmfsin.quepreferirias.domain.usecases

import com.mmfsin.quepreferirias.base.BaseUseCase
import com.mmfsin.quepreferirias.domain.interfaces.IDilemmasRepository
import com.mmfsin.quepreferirias.domain.models.CommentVote
import com.mmfsin.quepreferirias.domain.models.CommentVote.VOTE_DOWN
import com.mmfsin.quepreferirias.domain.models.CommentVote.VOTE_UP
import javax.inject.Inject

class VoteDilemmaUseCase @Inject constructor(
    private val repository: IDilemmasRepository
) : BaseUseCase<VoteDilemmaUseCase.Params, Unit>() {

    override suspend fun execute(params: Params) {

    }

    data class Params(
        val dilemmaId: String,
    )
}