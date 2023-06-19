package com.mmfsin.quepreferirias.presentation.dashboard

import com.mmfsin.quepreferirias.base.BaseViewModel
import com.mmfsin.quepreferirias.domain.usecases.GetAppDataUseCase
import com.mmfsin.quepreferirias.domain.usecases.GetPercentsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getAppDataUseCase: GetAppDataUseCase,
    private val getPercentsUseCase: GetPercentsUseCase
) : BaseViewModel<DashboardEvent>() {

    fun getAppData() {
        executeUseCase(
            { getAppDataUseCase.execute() },
            { result ->
                _event.value =
                    if (result.isEmpty()) DashboardEvent.SWW else DashboardEvent.AppData(result)
            },
            { _event.value = DashboardEvent.SWW }
        )
    }

    fun getPercents(votesYes: Long, votesNo: Long) {
        executeUseCase(
            { getPercentsUseCase.execute(GetPercentsUseCase.Params(votesYes, votesNo)) },
            { result ->
                _event.value =
                    result?.let { DashboardEvent.GetPercents(it) } ?: run { DashboardEvent.SWW }
            },
            { _event.value = DashboardEvent.SWW }
        )
    }
}