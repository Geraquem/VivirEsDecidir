package com.mmfsin.quepreferirias.domain.usecases

import com.mmfsin.quepreferirias.base.BaseUseCase
import com.mmfsin.quepreferirias.domain.interfaces.IUserRepository
import com.mmfsin.quepreferirias.domain.models.Session
import javax.inject.Inject

class GetUserByIdUseCase @Inject constructor(
    private val repository: IUserRepository
) : BaseUseCase<GetUserByIdUseCase.Params, Session?>() {

    override suspend fun execute(params: Params): Session? = repository.getUserSession()

    data class Params(
        val userId: String
    )
}