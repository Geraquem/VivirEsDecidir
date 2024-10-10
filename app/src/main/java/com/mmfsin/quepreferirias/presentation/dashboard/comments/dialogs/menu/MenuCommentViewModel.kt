package com.mmfsin.quepreferirias.presentation.dashboard.comments.dialogs.menu

import com.mmfsin.quepreferirias.base.BaseViewModel
import com.mmfsin.quepreferirias.domain.usecases.DeleteDataCommentUseCase
import com.mmfsin.quepreferirias.domain.usecases.GetSessionUseCase
import com.mmfsin.quepreferirias.presentation.models.DashboardType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MenuCommentViewModel @Inject constructor(
    private val getSessionUseCase: GetSessionUseCase,
    private val deleteDataCommentUseCase: DeleteDataCommentUseCase,
) : BaseViewModel<MenuCommentEvent>() {

    fun getSession() {
        executeUseCase(
            { getSessionUseCase.execute() },
            { result -> _event.value = MenuCommentEvent.UserData(result) },
            { _event.value = MenuCommentEvent.SWW }
        )
    }

    fun deleteComment(dilemmaId: String, type: DashboardType, commentId: String) {
        executeUseCase(
            {
                deleteDataCommentUseCase.execute(
                    DeleteDataCommentUseCase.Params(dilemmaId, type, commentId)
                )
            },
            { result -> _event.value = MenuCommentEvent.CommentDeleted(result) },
            { _event.value = MenuCommentEvent.SWW }
        )
    }
}