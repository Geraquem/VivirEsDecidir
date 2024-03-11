package com.mmfsin.quepreferirias.presentation.dashboard.dilemmas.comments

import com.mmfsin.quepreferirias.base.BaseViewModel
import com.mmfsin.quepreferirias.domain.models.CommentVote
import com.mmfsin.quepreferirias.domain.models.Session
import com.mmfsin.quepreferirias.domain.usecases.GetDilemmaCommentsUseCase
import com.mmfsin.quepreferirias.domain.usecases.GetSessionUseCase
import com.mmfsin.quepreferirias.domain.usecases.SendDilemmaCommentUseCase
import com.mmfsin.quepreferirias.domain.usecases.VoteDilemmaCommentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CommentsViewModel @Inject constructor(
    private val getSessionUseCase: GetSessionUseCase,
    private val getDilemmaCommentsUseCase: GetDilemmaCommentsUseCase,
    private val setDilemmaCommentUseCase: SendDilemmaCommentUseCase,
    private val voteDilemmaCommentUseCase: VoteDilemmaCommentUseCase
) : BaseViewModel<CommentsEvent>() {

    fun getUserData() {
        executeUseCase(
            { getSessionUseCase.execute() },
            { result -> _event.value = result?.let { CommentsEvent.GetUserData(it) } },
            { _event.value = CommentsEvent.SWW }
        )
    }

    fun getComments() {
        executeUseCase(
            {
                getDilemmaCommentsUseCase.execute(
                    GetDilemmaCommentsUseCase.Params(fromRealm = true)
                )
            },
            { result -> _event.value = CommentsEvent.Comments(result) },
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
            { result -> _event.value = CommentsEvent.CommentSentResult(result) },
            { _event.value = CommentsEvent.SWW }
        )
    }

    fun voteComment(
        dilemmaId: String,
        commentId: String,
        vote: CommentVote,
        likes: Long,
        position: Int
    ) {
        executeUseCase(
            {
                voteDilemmaCommentUseCase.execute(
                    VoteDilemmaCommentUseCase.Params(dilemmaId, commentId, vote, likes)
                )
            },
            { _event.value = CommentsEvent.CommentVotedResult(commentId, vote, position) },
            { _event.value = CommentsEvent.SWW }
        )
    }
}