package com.mmfsin.quepreferirias.presentation.dashboard.comments.dialogs.menu.replies

import com.mmfsin.quepreferirias.base.BaseViewModel
import com.mmfsin.quepreferirias.domain.usecases.DeleteDataCommentReplyUseCase
import com.mmfsin.quepreferirias.domain.usecases.GetSessionUseCase
import com.mmfsin.quepreferirias.presentation.models.DashboardType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MenuRepliesViewModel @Inject constructor(
    private val getSessionUseCase: GetSessionUseCase,
    private val deleteDataCommentReplyUseCase: DeleteDataCommentReplyUseCase
) : BaseViewModel<MenuRepliesEvent>() {

    fun getSession() {
        executeUseCase(
            { getSessionUseCase.execute() },
            { result -> _event.value = MenuRepliesEvent.UserData(result) },
            { _event.value = MenuRepliesEvent.SWW }
        )
    }

    fun deleteReply(dataId: String, type: DashboardType, commentId: String, replyId: String) {
        executeUseCase(
            {
                deleteDataCommentReplyUseCase.execute(
                    DeleteDataCommentReplyUseCase.Params(dataId, type, commentId, replyId)
                )
            },
            { result -> _event.value = MenuRepliesEvent.ReplyDeleted(result) },
            { _event.value = MenuRepliesEvent.SWW }
        )
    }
}