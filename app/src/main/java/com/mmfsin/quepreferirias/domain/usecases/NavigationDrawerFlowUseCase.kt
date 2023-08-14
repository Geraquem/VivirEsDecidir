package com.mmfsin.quepreferirias.domain.usecases

import android.content.Context
import com.mmfsin.quepreferirias.base.BaseUseCase
import com.mmfsin.quepreferirias.presentation.models.DrawerFlow
import com.mmfsin.quepreferirias.utils.SESSION
import com.mmfsin.quepreferirias.utils.SESSION_INITIATED
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class NavigationDrawerFlowUseCase @Inject constructor(
    @ApplicationContext val context: Context,
) : BaseUseCase<NavigationDrawerFlowUseCase.Params, Pair<Boolean, DrawerFlow>>() {

    override suspend fun execute(params: Params): Pair<Boolean, DrawerFlow> {
        val session = context.getSharedPreferences(SESSION, Context.MODE_PRIVATE)
        val isInitiated = session.getBoolean(SESSION_INITIATED, false)
        return Pair(isInitiated, params.flow)
    }

    data class Params(
        val flow: DrawerFlow
    )
}