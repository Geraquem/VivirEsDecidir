package com.mmfsin.quepreferirias.domain.usecases

import com.mmfsin.quepreferirias.base.BaseUseCaseNoParams
import com.mmfsin.quepreferirias.domain.interfaces.IDilemmasRepository
import com.mmfsin.quepreferirias.domain.interfaces.IDualismsRepository
import com.mmfsin.quepreferirias.domain.models.DilemmaFav
import com.mmfsin.quepreferirias.domain.models.DualismFav
import javax.inject.Inject

class GetFavDualismsUseCase @Inject constructor(
    private val repository: IDualismsRepository
) : BaseUseCaseNoParams<List<DualismFav>>() {

    override suspend fun execute(): List<DualismFav> = repository.getFavDualisms()
}