package com.mmfsin.quepreferirias.presentation.dashboard.dilemmas.comments.dialogs.send

import com.mmfsin.quepreferirias.base.BaseViewModel
import com.mmfsin.quepreferirias.domain.models.Session
import com.mmfsin.quepreferirias.domain.usecases.SendDilemmaCommentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SendCommentViewModel @Inject constructor(
    private val setDilemmaCommentUseCase: SendDilemmaCommentUseCase
) : BaseViewModel<SendCommentEvent>() {

    fun sendComment(dilemmaId: String, session: Session, comment: String) {
        executeUseCase(
            {
                setDilemmaCommentUseCase.execute(
                    SendDilemmaCommentUseCase.Params(dilemmaId, session, comment)
                )
            },
            { result ->
                _event.value = if (result) SendCommentEvent.CommentSent
                else SendCommentEvent.SWW
            },
            { _event.value = SendCommentEvent.SWW }
        )
    }
}