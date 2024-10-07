package com.mmfsin.quepreferirias.presentation.dashboard.dilemmas.comments.dialogs.menu

import com.mmfsin.quepreferirias.base.BaseViewModel
import com.mmfsin.quepreferirias.domain.usecases.GetSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MenuCommentViewModel @Inject constructor(
    private val getSessionUseCase: GetSessionUseCase
) : BaseViewModel<MenuCommentEvent>() {

    fun getSession() {
        executeUseCase(
            { getSessionUseCase.execute() },
            { result -> _event.value = MenuCommentEvent.UserData(result) },
            { _event.value = MenuCommentEvent.SWW }
        )
    }
}