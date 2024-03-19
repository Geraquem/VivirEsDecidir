package com.mmfsin.quepreferirias.domain.usecases

import android.content.Context
import com.mmfsin.quepreferirias.base.BaseUseCaseNoParams
import com.mmfsin.quepreferirias.domain.interfaces.IUserRepository
import com.mmfsin.quepreferirias.domain.models.Session
import com.mmfsin.quepreferirias.utils.SESSION
import com.mmfsin.quepreferirias.utils.SESSION_INITIATED
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GetSessionUseCase @Inject constructor(
    @ApplicationContext val context: Context,
    private val repository: IUserRepository
) : BaseUseCaseNoParams<Session?>() {

    override suspend fun execute(): Session? {
        val session = context.getSharedPreferences(SESSION, Context.MODE_PRIVATE)
        val isInitiated = session.getBoolean(SESSION_INITIATED, false)
        if (!isInitiated) return null
        val userSession = repository.getSession()
        userSession?.let { return it } ?: run {
            session.edit().apply {
                putBoolean(SESSION_INITIATED, false)
                apply()
            }
            return null
        }
    }
}