package com.mmfsin.quepreferirias.domain.usecases

import com.mmfsin.quepreferirias.base.BaseUseCase
import com.mmfsin.quepreferirias.domain.interfaces.IUserRepository
import com.mmfsin.quepreferirias.domain.models.RRSS
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(
    val repository: IUserRepository,
) : BaseUseCase<UpdateProfileUseCase.Params, Unit>() {

    override suspend fun execute(params: Params) = repository.updateProfile(params.rrss)

    data class Params(
        val rrss: RRSS
    )
}