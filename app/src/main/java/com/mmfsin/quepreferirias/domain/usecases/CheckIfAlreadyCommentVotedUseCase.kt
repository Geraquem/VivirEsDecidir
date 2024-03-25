package com.mmfsin.quepreferirias.domain.usecases

import com.mmfsin.quepreferirias.base.BaseUseCase
import com.mmfsin.quepreferirias.domain.interfaces.IDilemmasRepository
import com.mmfsin.quepreferirias.domain.models.CommentVote
import javax.inject.Inject

class CheckIfAlreadyCommentVotedUseCase @Inject constructor(
    private val repository: IDilemmasRepository
) : BaseUseCase<CheckIfAlreadyCommentVotedUseCase.Params, Boolean>() {

    override suspend fun execute(params: Params): Boolean =
        repository.alreadyCommentVoted(params.commentId, params.vote)

    data class Params(
        val commentId: String,
        val vote: CommentVote
    )
}