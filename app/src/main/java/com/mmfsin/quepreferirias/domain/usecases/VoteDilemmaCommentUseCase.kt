package com.mmfsin.quepreferirias.domain.usecases

import com.mmfsin.quepreferirias.base.BaseUseCase
import com.mmfsin.quepreferirias.domain.interfaces.IDilemmasRepository
import com.mmfsin.quepreferirias.domain.models.CommentVote
import com.mmfsin.quepreferirias.domain.models.CommentVote.VOTE_DOWN
import com.mmfsin.quepreferirias.domain.models.CommentVote.VOTE_UP
import javax.inject.Inject

class VoteDilemmaCommentUseCase @Inject constructor(
    private val repository: IDilemmasRepository
) : BaseUseCase<VoteDilemmaCommentUseCase.Params, Unit>() {

    override suspend fun execute(params: Params) {
        val actualLikes = params.likes
        val newLikes = when (params.vote) {
            VOTE_UP -> actualLikes.plus(1)
            VOTE_DOWN -> actualLikes.minus(1)
        }
        repository.voteDilemmaComment(params.dilemmaId, params.commentId, newLikes, params.vote)
    }

    data class Params(
        val dilemmaId: String,
        val commentId: String,
        val vote: CommentVote,
        val likes: Long
    )
}