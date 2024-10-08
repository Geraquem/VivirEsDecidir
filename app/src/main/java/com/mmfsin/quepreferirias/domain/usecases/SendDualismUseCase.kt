package com.mmfsin.quepreferirias.domain.usecases

import com.mmfsin.quepreferirias.base.BaseUseCase
import com.mmfsin.quepreferirias.data.mappers.toSendDualismDTO
import com.mmfsin.quepreferirias.domain.interfaces.IDualismsRepository
import com.mmfsin.quepreferirias.domain.models.SendDualism
import java.util.UUID
import javax.inject.Inject

class SendDualismUseCase @Inject constructor(
    private val repository: IDualismsRepository
) : BaseUseCase<SendDualismUseCase.Params, Unit>() {

    override suspend fun execute(params: Params) {
        val dualism = SendDualism(
            dualismId = UUID.randomUUID().toString(),
            explanation = params.explanation,
            txtTop = params.txtTop,
            txtBottom = params.txtBottom,
            creatorId = params.creatorId,
            creatorName = params.creatorName,
            timestamp = System.currentTimeMillis(),
            filterValue = Math.random()
        )
        return repository.sendDualism(dualism.toSendDualismDTO())
    }

    data class Params(
        val explanation: String?,
        val txtTop: String,
        val txtBottom: String,
        val creatorId: String,
        val creatorName: String
    )
}