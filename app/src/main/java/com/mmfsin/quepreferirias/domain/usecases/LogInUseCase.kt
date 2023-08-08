package com.mmfsin.quepreferirias.domain.usecases

import android.content.Context
import com.mmfsin.quepreferirias.base.BaseUseCaseNoParams
import com.mmfsin.quepreferirias.utils.SESSION
import com.mmfsin.quepreferirias.utils.SESSION_INITIATED
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class LogInUseCase @Inject constructor(
    @ApplicationContext val context: Context,
) : BaseUseCaseNoParams<Unit>() {

    override suspend fun execute(): Unit {
        val session = context.getSharedPreferences(SESSION, Context.MODE_PRIVATE)
        session.getBoolean(SESSION_INITIATED, false)
    }
}