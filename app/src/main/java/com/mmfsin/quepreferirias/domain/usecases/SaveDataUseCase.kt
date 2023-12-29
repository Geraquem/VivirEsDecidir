package com.mmfsin.quepreferirias.domain.usecases

import android.content.Context
import com.mmfsin.quepreferirias.base.BaseUseCase
import com.mmfsin.quepreferirias.domain.interfaces.IUserRepository
import com.mmfsin.quepreferirias.utils.SESSION
import com.mmfsin.quepreferirias.utils.SESSION_INITIATED
import com.mmfsin.quepreferirias.utils.UPDATE_SAVED_DATA
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SaveDataUseCase @Inject constructor(
    @ApplicationContext val context: Context,
    private val sessionRepository: IUserRepository
) : BaseUseCase<SaveDataUseCase.Params, Boolean?>() {

    override suspend fun execute(params: Params): Boolean? {
        val session = context.getSharedPreferences(SESSION, Context.MODE_PRIVATE)
        val isInitiated = session.getBoolean(SESSION_INITIATED, false)
        if (!isInitiated) return null
        session.edit().apply() {
            putBoolean(UPDATE_SAVED_DATA, true)
            apply()
        }
        return sessionRepository.saveData(params.dataId)
    }

    data class Params(
        val dataId: String
    )
}