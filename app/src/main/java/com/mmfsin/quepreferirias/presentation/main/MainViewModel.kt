package com.mmfsin.quepreferirias.presentation.main

import com.mmfsin.quepreferirias.base.BaseViewModel
import com.mmfsin.quepreferirias.domain.usecases.LogInGoogleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val logInGoogleUseCase: LogInGoogleUseCase
) : BaseViewModel<MainEvent>() {

    fun doLogin() {
        executeUseCase(
            { logInGoogleUseCase.execute() },
            { result -> _event.value = MainEvent.GoogleClient(result) },
            { _event.value = MainEvent.SWW }
        )
    }

}