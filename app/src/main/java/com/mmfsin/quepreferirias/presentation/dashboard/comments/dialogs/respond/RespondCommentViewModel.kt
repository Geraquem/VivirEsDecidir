package com.mmfsin.quepreferirias.presentation.dashboard.comments.dialogs.respond

import com.mmfsin.quepreferirias.base.BaseViewModel
import com.mmfsin.quepreferirias.domain.models.Session
import com.mmfsin.quepreferirias.domain.usecases.SendDataCommentUseCase
import com.mmfsin.quepreferirias.presentation.models.DashboardType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RespondCommentViewModel @Inject constructor(
    private val setDilemmaCommentUseCase: SendDataCommentUseCase
) : BaseViewModel<RespondCommentEvent>() {

    fun respondComment(dilemmaId: String, type: DashboardType, session: Session, comment: String) {
//        executeUseCase(
//            {
//                setDilemmaCommentUseCase.execute(
//                    SendDataCommentUseCase.Params(dilemmaId, type, session, comment)
//                )
//            },
//            { result ->
//                _event.value = result?.let { RespondCommentEvent.CommentReplied(result) }
//                    ?: RespondCommentEvent.SWW
//            },
//            { _event.value = RespondCommentEvent.SWW }
//        )
    }
}