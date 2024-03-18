package com.mmfsin.quepreferirias.domain.usecases

import com.mmfsin.quepreferirias.base.BaseUseCase
import com.mmfsin.quepreferirias.data.mappers.toSendDilemmaDTO
import com.mmfsin.quepreferirias.domain.interfaces.IDilemmasRepository
import com.mmfsin.quepreferirias.domain.models.SendDilemma
import com.mmfsin.quepreferirias.domain.models.Session
import java.util.UUID
import javax.inject.Inject

class SendDilemmaUseCase @Inject constructor(private val repository: IDilemmasRepository) :
    BaseUseCase<SendDilemmaUseCase.Params, Boolean>() {

    override suspend fun execute(params: Params): Boolean {
        val dilemma = SendDilemma(
            dilemmaId = UUID.randomUUID().toString(),
            txtTop = params.txtTop,
            txtBottom = params.txtBottom,
            creatorId = params.creatorId,
            creatorName = params.creatorName,
            timestamp = System.currentTimeMillis()
        )
        return repository.sendDilemma(dilemma.toSendDilemmaDTO())
    }

    data class Params(
        val txtTop: String,
        val txtBottom: String,
        val creatorId: String,
        val creatorName: String
    )
}