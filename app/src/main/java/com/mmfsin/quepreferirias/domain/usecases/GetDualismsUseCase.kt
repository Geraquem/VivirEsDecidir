package com.mmfsin.quepreferirias.domain.usecases

import com.mmfsin.quepreferirias.base.BaseUseCaseNoParams
import com.mmfsin.quepreferirias.domain.interfaces.IDualismsRepository
import com.mmfsin.quepreferirias.domain.models.Dualism
import javax.inject.Inject

class GetDualismsUseCase @Inject constructor(
    private val repository: IDualismsRepository
) : BaseUseCaseNoParams<List<Dualism>>() {

    override suspend fun execute(): List<Dualism> = repository.getDualisms()//.shuffled()
}