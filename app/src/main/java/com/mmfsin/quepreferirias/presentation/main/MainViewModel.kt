package com.mmfsin.quepreferirias.presentation.main

import com.mmfsin.quepreferirias.base.BaseViewModel
import com.mmfsin.quepreferirias.domain.usecases.GetSessionUseCase
import com.mmfsin.quepreferirias.domain.usecases.NavigationDrawerFlowUseCase
import com.mmfsin.quepreferirias.presentation.models.DrawerFlow
import com.mmfsin.quepreferirias.presentation.profile.ProfileEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val navigationDrawerFlowUseCase: NavigationDrawerFlowUseCase,
    private val getSessionUseCase: GetSessionUseCase
) : BaseViewModel<MainEvent>() {

    fun checkSession(flow: DrawerFlow) {
        executeUseCase(
            { navigationDrawerFlowUseCase.execute(NavigationDrawerFlowUseCase.Params(flow)) },
            { result -> _event.value = MainEvent.DrawerFlowDirection(result) },
            { _event.value = MainEvent.SWW }
        )
    }

    fun getSession() {
        executeUseCase(
            { getSessionUseCase.execute() },
            { result -> _event.value = MainEvent.GetSession(result) },
            { _event.value = MainEvent.SWW }
        )
    }
}