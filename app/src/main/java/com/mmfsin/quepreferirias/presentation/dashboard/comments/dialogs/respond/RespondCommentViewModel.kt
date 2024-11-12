package com.mmfsin.quepreferirias.presentation.dashboard.comments.dialogs.respond

import com.mmfsin.quepreferirias.base.BaseViewModel
import com.mmfsin.quepreferirias.domain.models.Session
import com.mmfsin.quepreferirias.domain.usecases.RespondDataCommentUseCase
import com.mmfsin.quepreferirias.domain.usecases.SendDataCommentUseCase
import com.mmfsin.quepreferirias.presentation.models.DashboardType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RespondCommentViewModel @Inject constructor(
    private val respondDataCommentUseCase: RespondDataCommentUseCase
) : BaseViewModel<RespondCommentEvent>() {

    fun respondComment(
        dataId: String,
        commentId: String,
        type: DashboardType,
        session: Session,
        reply: String
    ) {
        executeUseCase(
            {
                respondDataCommentUseCase.execute(
                    RespondDataCommentUseCase.Params(dataId, commentId, type, session, reply)
                )
            },
            { result ->
                _event.value = result?.let { RespondCommentEvent.CommentReplied(result) }
                    ?: RespondCommentEvent.SWW
            },
            { _event.value = RespondCommentEvent.SWW }
        )
    }
}