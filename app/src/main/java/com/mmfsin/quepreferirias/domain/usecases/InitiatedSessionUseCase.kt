package com.mmfsin.quepreferirias.domain.usecases

import android.content.Context
import com.mmfsin.quepreferirias.base.BaseUseCaseNoParams
import com.mmfsin.quepreferirias.utils.SESSION
import com.mmfsin.quepreferirias.utils.SESSION_INITIATED
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class InitiatedSessionUseCase @Inject constructor(
    @ApplicationContext val context: Context
) : BaseUseCaseNoParams<Boolean>() {

    override suspend fun execute(): Boolean {
        val session = context.getSharedPreferences(SESSION, Context.MODE_PRIVATE)
        return session.getBoolean(SESSION_INITIATED, false)
    }
}