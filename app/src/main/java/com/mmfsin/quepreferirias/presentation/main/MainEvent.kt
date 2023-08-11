package com.mmfsin.quepreferirias.presentation.main

import com.mmfsin.quepreferirias.presentation.models.DrawerFlow

sealed class MainEvent {
    class DrawerFlowDirection(val result: Pair<Boolean, DrawerFlow>) : MainEvent()
    object SWW : MainEvent()
}