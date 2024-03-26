package com.mmfsin.quepreferirias.domain.usecases

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.mmfsin.quepreferirias.base.BaseUseCase
import com.mmfsin.quepreferirias.domain.interfaces.IUserRepository
import com.mmfsin.quepreferirias.domain.models.Session
import com.mmfsin.quepreferirias.utils.SESSION
import com.mmfsin.quepreferirias.utils.SESSION_INITIATED
import com.mmfsin.quepreferirias.utils.UPDATE_SAVED_DATA
import com.mmfsin.quepreferirias.utils.UPDATE_SENT_DATA
import com.mmfsin.quepreferirias.utils.UPDATE_USER_DATA
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SaveSessionUseCase @Inject constructor(
    @ApplicationContext val context: Context,
    private val repository: IUserRepository
) : BaseUseCase<SaveSessionUseCase.Params, Boolean>() {

    override suspend fun execute(params: Params): Boolean {
        val result = repository.saveSession(params.session)
        if (result) {
            /** Shared prefs */
            val session = context.getSharedPreferences(SESSION, MODE_PRIVATE)
            session.edit().apply() {
                putBoolean(SESSION_INITIATED, true)
                putBoolean(UPDATE_USER_DATA, true)
                putBoolean(UPDATE_SAVED_DATA, true)
                putBoolean(UPDATE_SENT_DATA, true)
                apply()
            }
        }
        return result
    }

    data class Params(
        val session: Session
    )
}