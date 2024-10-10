package com.mmfsin.quepreferirias.domain.usecases

import com.mmfsin.quepreferirias.base.BaseUseCase
import com.mmfsin.quepreferirias.domain.interfaces.ICommentsRepository
import com.mmfsin.quepreferirias.domain.models.Comment
import com.mmfsin.quepreferirias.presentation.models.DashboardType
import com.mmfsin.quepreferirias.utils.DILEMMAS
import com.mmfsin.quepreferirias.utils.DUALISMS
import javax.inject.Inject

class GetDataCommentsUseCase @Inject constructor(
    val repository: ICommentsRepository
) : BaseUseCase<GetDataCommentsUseCase.Params, List<Comment>>() {

    override suspend fun execute(params: Params): List<Comment> {
        val root = when (params.type) {
            DashboardType.DILEMMA -> DILEMMAS
            DashboardType.DUALISM -> DUALISMS
        }
        return repository.getComments(params.dataId, root)
    }

    data class Params(
        val dataId: String,
        val type: DashboardType
    )
}