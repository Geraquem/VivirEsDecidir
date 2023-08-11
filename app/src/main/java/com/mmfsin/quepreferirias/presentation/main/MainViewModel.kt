package com.mmfsin.quepreferirias.presentation.main

import com.mmfsin.quepreferirias.base.BaseViewModel
import com.mmfsin.quepreferirias.domain.usecases.GetSessionInitiatedUseCase
import com.mmfsin.quepreferirias.presentation.models.DrawerFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val sessionInitiatedUseCase: GetSessionInitiatedUseCase
) : BaseViewModel<MainEvent>() {

    fun checkSession(flow: DrawerFlow) {
        executeUseCase(
            { sessionInitiatedUseCase.execute(GetSessionInitiatedUseCase.Params(flow)) },
            { result -> _event.value = MainEvent.DrawerFlowDirection(result) },
            { _event.value = MainEvent.SWW }
        )
    }
}