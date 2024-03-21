package com.mmfsin.quepreferirias.presentation.main

import com.mmfsin.quepreferirias.domain.models.Session
import com.mmfsin.quepreferirias.presentation.models.DrawerFlow

sealed class MainEvent {
    class DrawerFlowDirection(val result: Pair<Boolean, DrawerFlow>) : MainEvent()
    class GetSession(val session: Session?) : MainEvent()
    object SWW : MainEvent()
}