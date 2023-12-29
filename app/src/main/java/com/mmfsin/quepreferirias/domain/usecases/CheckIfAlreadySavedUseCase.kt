package com.mmfsin.quepreferirias.domain.usecases

import android.content.Context
import com.mmfsin.quepreferirias.base.BaseUseCase
import com.mmfsin.quepreferirias.domain.interfaces.IUserRepository
import com.mmfsin.quepreferirias.utils.SESSION
import com.mmfsin.quepreferirias.utils.SESSION_INITIATED
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class CheckIfAlreadySavedUseCase @Inject constructor(
    @ApplicationContext val context: Context,
    private val sessionRepository: IUserRepository
) :
    BaseUseCase<CheckIfAlreadySavedUseCase.Params, Boolean?>() {

    override suspend fun execute(params: Params): Boolean? {
        val session = context.getSharedPreferences(SESSION, Context.MODE_PRIVATE)
        val isInitiated = session.getBoolean(SESSION_INITIATED, false)
        if (!isInitiated) return null
        return sessionRepository.checkIfIsSavedData(params.dataId)
    }

    data class Params(
        val dataId: String
    )
}