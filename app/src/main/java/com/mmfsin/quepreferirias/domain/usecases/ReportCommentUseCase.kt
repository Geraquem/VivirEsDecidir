package com.mmfsin.quepreferirias.domain.usecases

import com.mmfsin.quepreferirias.base.BaseUseCase
import com.mmfsin.quepreferirias.domain.interfaces.ICommentsRepository
import com.mmfsin.quepreferirias.domain.models.Comment
import javax.inject.Inject

class ReportCommentUseCase @Inject constructor(
    private val repository: ICommentsRepository
) : BaseUseCase<ReportCommentUseCase.Params, Unit>() {

    override suspend fun execute(params: Params) =
        repository.reportComment(params.dataId, params.comment)

    data class Params(
        val dataId: String,
        val comment: Comment
    )
}