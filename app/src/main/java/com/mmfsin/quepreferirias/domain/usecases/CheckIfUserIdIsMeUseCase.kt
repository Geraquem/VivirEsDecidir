package com.mmfsin.quepreferirias.domain.usecases

import com.mmfsin.quepreferirias.base.BaseUseCase
import com.mmfsin.quepreferirias.domain.interfaces.IUserRepository
import javax.inject.Inject

class CheckIfUserIdIsMeUseCase @Inject constructor(
    private val repository: IUserRepository
) : BaseUseCase<CheckIfUserIdIsMeUseCase.Params, Boolean>() {

    override suspend fun execute(params: Params): Boolean {
        val session = repository.getUserSession()
        return session?.let { it.id == params.userId } ?: run { false }
    }

    data class Params(
        val userId: String,
    )
}