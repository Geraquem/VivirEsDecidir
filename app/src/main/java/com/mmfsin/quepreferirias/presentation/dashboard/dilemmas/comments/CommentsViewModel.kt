package com.mmfsin.quepreferirias.presentation.dashboard.dilemmas.comments

import com.mmfsin.quepreferirias.base.BaseViewModel
import com.mmfsin.quepreferirias.domain.models.Session
import com.mmfsin.quepreferirias.domain.usecases.GetSessionUseCase
import com.mmfsin.quepreferirias.domain.usecases.SendDilemmaCommentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CommentsViewModel @Inject constructor(
    private val getSessionUseCase: GetSessionUseCase,
    private val setDilemmaCommentUseCase: SendDilemmaCommentUseCase
) : BaseViewModel<CommentsEvent>() {

    fun getUserData() {
        executeUseCase(
            { getSessionUseCase.execute() },
            { result -> _event.value = result?.let { CommentsEvent.GetUserData(it) } },
            { _event.value = CommentsEvent.SWW }
        )
    }

    fun sendComment(dilemmaId: String, session: Session, comment: String) {
        executeUseCase(
            {
                setDilemmaCommentUseCase.execute(
                    SendDilemmaCommentUseCase.Params(dilemmaId, session, comment)
                )
            },
            { result -> _event.value = CommentsEvent.CommentResult(result) },
            { _event.value = CommentsEvent.SWW }
        )
    }
}