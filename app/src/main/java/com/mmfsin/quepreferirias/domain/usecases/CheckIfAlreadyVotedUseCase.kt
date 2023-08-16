package com.mmfsin.quepreferirias.domain.usecases

import android.content.Context
import com.mmfsin.quepreferirias.base.BaseUseCase
import com.mmfsin.quepreferirias.domain.interfaces.ISessionRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class CheckIfAlreadyVotedUseCase @Inject constructor(
    @ApplicationContext val context: Context,
    private val sessionRepository: ISessionRepository
) :
    BaseUseCase<CheckIfAlreadyVotedUseCase.Params, Boolean?>() {

    override suspend fun execute(params: Params): Boolean? {
        /** TODOOOOOO */
        return null
    }

    data class Params(
        val dataId: String
    )
}