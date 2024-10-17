package com.mmfsin.quepreferirias.domain.usecases

import com.mmfsin.quepreferirias.base.BaseUseCase
import com.mmfsin.quepreferirias.domain.interfaces.IDualismsRepository
import com.mmfsin.quepreferirias.domain.models.Dualism
import javax.inject.Inject

class GetDualismByIdUseCase @Inject constructor(
    private val repository: IDualismsRepository
) : BaseUseCase<GetDualismByIdUseCase.Params, Dualism?>() {

    override suspend fun execute(params: Params): Dualism? =
        repository.getDualismById(params.dualismId)

    data class Params(
        val dualismId: String,
    )
}