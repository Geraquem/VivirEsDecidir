package com.mmfsin.quepreferirias.domain.usecases

import com.mmfsin.quepreferirias.base.BaseUseCase
import com.mmfsin.quepreferirias.domain.interfaces.ICommentsRepository
import com.mmfsin.quepreferirias.presentation.models.DashboardType
import com.mmfsin.quepreferirias.utils.DILEMMAS
import com.mmfsin.quepreferirias.utils.DUALISMS
import javax.inject.Inject

class DeleteDataCommentReplyUseCase @Inject constructor(
    private val repository: ICommentsRepository
) : BaseUseCase<DeleteDataCommentReplyUseCase.Params, Boolean>() {

    override suspend fun execute(params: Params): Boolean {
        val root = when (params.type) {
            DashboardType.DILEMMA -> DILEMMAS
            DashboardType.DUALISM -> DUALISMS
        }
        return repository.deleteCommentReply(params.dataId, root, params.commentId, params.replyId)
    }

    data class Params(
        val dataId: String,
        val type: DashboardType,
        val commentId: String,
        val replyId: String,
    )
}