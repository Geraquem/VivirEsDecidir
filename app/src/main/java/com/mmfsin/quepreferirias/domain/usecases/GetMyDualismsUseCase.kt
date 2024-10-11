package com.mmfsin.quepreferirias.domain.usecases

import com.mmfsin.quepreferirias.base.BaseUseCaseNoParams
import com.mmfsin.quepreferirias.domain.interfaces.IDualismsRepository
import com.mmfsin.quepreferirias.domain.models.SendDualism
import javax.inject.Inject

class GetMyDualismsUseCase @Inject constructor(
    private val repository: IDualismsRepository
) : BaseUseCaseNoParams<List<SendDualism>>() {

    override suspend fun execute(): List<SendDualism> =
        repository.getMyDualisms().sortedBy { it.timestamp }.reversed()
}