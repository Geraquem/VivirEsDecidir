package com.mmfsin.quepreferirias.presentation.profile

import com.mmfsin.quepreferirias.base.BaseViewModel
import com.mmfsin.quepreferirias.domain.usecases.GetSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getSessionUseCase: GetSessionUseCase
) : BaseViewModel<ProfileEvent>() {

    fun getSession() {
        executeUseCase(
            { getSessionUseCase.execute() },
            { result ->
                _event.value = result?.let { ProfileEvent.Profile(result) } ?: ProfileEvent.SWW
            },
            { _event.value = ProfileEvent.SWW }
        )
    }
}