package com.mmfsin.quepreferirias.presentation.profile.otherprofile

import com.mmfsin.quepreferirias.base.BaseViewModel
import com.mmfsin.quepreferirias.domain.usecases.GetUserByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OtherProfileViewModel @Inject constructor(
    private val getUserByIdUseCase: GetUserByIdUseCase,
) : BaseViewModel<OtherProfileEvent>() {

    fun getUserById(userId: String) {
        executeUseCase(
            { getUserByIdUseCase.execute(GetUserByIdUseCase.Params(userId)) },
            { result ->
                _event.value = result?.let { OtherProfileEvent.Profile(it) }
                    ?: run { OtherProfileEvent.SWW }
            },
            { _event.value = OtherProfileEvent.SWW })
    }
}