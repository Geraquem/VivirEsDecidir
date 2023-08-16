package com.mmfsin.quepreferirias.presentation.dashboard

import com.mmfsin.quepreferirias.domain.models.Data
import com.mmfsin.quepreferirias.presentation.models.Percents

sealed class DashboardEvent {
    class AppData(val data: List<Data>) : DashboardEvent()
    class GetPercents(val percents: Percents) : DashboardEvent()
    class AlreadySaved(val saved: Boolean?) : DashboardEvent()
    class DataSaved(val result: Boolean?) : DashboardEvent()
    object SWW : DashboardEvent()
}