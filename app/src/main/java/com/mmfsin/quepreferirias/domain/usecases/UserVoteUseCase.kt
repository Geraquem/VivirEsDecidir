package com.mmfsin.quepreferirias.domain.usecases

import com.mmfsin.quepreferirias.base.BaseUseCase
import com.mmfsin.quepreferirias.domain.interfaces.IDataRepository
import com.mmfsin.quepreferirias.utils.VOTE_NO
import com.mmfsin.quepreferirias.utils.VOTE_YES
import javax.inject.Inject

class UserVoteUseCase @Inject constructor(private val repository: IDataRepository) :
    BaseUseCase<UserVoteUseCase.Params, Unit>() {

    override suspend fun execute(params: Params) {
        val voteId = if (params.isYes) VOTE_YES else VOTE_NO
        repository.vote(params.dataId, voteId)
    }

    data class Params(
        val dataId: String,
        val isYes: Boolean,
    )
}