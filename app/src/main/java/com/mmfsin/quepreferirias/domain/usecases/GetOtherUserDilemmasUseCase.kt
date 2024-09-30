package com.mmfsin.quepreferirias.domain.usecases

import com.mmfsin.quepreferirias.base.BaseUseCase
import com.mmfsin.quepreferirias.domain.interfaces.IDilemmasRepository
import com.mmfsin.quepreferirias.domain.models.SendDilemma
import javax.inject.Inject

class GetOtherUserDilemmasUseCase @Inject constructor(
    private val repository: IDilemmasRepository
) : BaseUseCase<GetOtherUserDilemmasUseCase.Params, List<SendDilemma>>() {

    override suspend fun execute(params: Params): List<SendDilemma> =
        repository.getOtherUserDilemmas(params.userId).sortedBy { it.timestamp }.reversed()

    data class Params(
        val userId: String
    )
}