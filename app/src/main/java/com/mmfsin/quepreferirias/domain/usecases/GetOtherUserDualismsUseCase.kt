package com.mmfsin.quepreferirias.domain.usecases

import com.mmfsin.quepreferirias.base.BaseUseCase
import com.mmfsin.quepreferirias.domain.interfaces.IDualismsRepository
import com.mmfsin.quepreferirias.domain.models.SendDualism
import javax.inject.Inject

class GetOtherUserDualismsUseCase @Inject constructor(
    private val repository: IDualismsRepository
) : BaseUseCase<GetOtherUserDualismsUseCase.Params, List<SendDualism>>() {

    override suspend fun execute(params: Params): List<SendDualism> =
        repository.getOtherUserDualisms(params.userId).sortedBy { it.timestamp }.reversed()

    data class Params(
        val userId: String
    )
}