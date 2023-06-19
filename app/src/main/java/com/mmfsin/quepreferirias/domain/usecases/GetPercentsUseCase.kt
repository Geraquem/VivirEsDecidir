package com.mmfsin.quepreferirias.domain.usecases

import com.mmfsin.quepreferirias.base.BaseUseCase
import com.mmfsin.quepreferirias.domain.interfaces.IDataRepository
import com.mmfsin.quepreferirias.presentation.models.Percents
import javax.inject.Inject

class GetPercentsUseCase @Inject constructor(private val repository: IDataRepository) :
    BaseUseCase<GetPercentsUseCase.Params, Percents?>() {

    override suspend fun execute(params: Params): Percents? {
        return try {
            val total = params.votesYes + params.votesNo
            val percentYes = params.votesYes * 100.0f / total
            val percentNo = params.votesNo * 100.0f / total
            val percents = Percents("", "")
            if (percentYes.toString().length >= 5 && percentNo.toString().length >= 5) {
                percents.percentYes = percentYes.toString().substring(0, 5) + "%"
                percents.percentNo = percentNo.toString().substring(0, 5) + "%"
            } else {
                percents.percentYes = "$percentYes%"
                percents.percentNo = "$percentNo%"
            }
            percents
        } catch (e: Exception) {
            null
        }
    }

    data class Params(
        val votesYes: Long,
        val votesNo: Long,
    )
}