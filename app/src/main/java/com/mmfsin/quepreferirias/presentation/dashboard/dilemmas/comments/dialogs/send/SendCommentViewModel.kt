package com.mmfsin.quepreferirias.presentation.dashboard.dilemmas.comments.dialogs.send

import com.mmfsin.quepreferirias.base.BaseViewModel
import com.mmfsin.quepreferirias.domain.models.Session
import com.mmfsin.quepreferirias.domain.usecases.SendDilemmaCommentUseCase
import com.mmfsin.quepreferirias.presentation.models.DashboardType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SendCommentViewModel @Inject constructor(
    private val setDilemmaCommentUseCase: SendDilemmaCommentUseCase
) : BaseViewModel<SendCommentEvent>() {

    fun sendComment(dilemmaId: String, type: DashboardType, session: Session, comment: String) {
        executeUseCase(
            {
                setDilemmaCommentUseCase.execute(
                    SendDilemmaCommentUseCase.Params(dilemmaId, type, session, comment)
                )
            },
            { result ->
                _event.value = result?.let { SendCommentEvent.CommentSent(result) }
                    ?: SendCommentEvent.SWW
            },
            { _event.value = SendCommentEvent.SWW }
        )
    }
}