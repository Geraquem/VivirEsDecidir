package com.mmfsin.quepreferirias.presentation.dashboard.dilemmas.comments.old

import com.mmfsin.quepreferirias.base.BaseViewModel
import com.mmfsin.quepreferirias.domain.models.CommentAlreadyVoted
import com.mmfsin.quepreferirias.domain.models.CommentVote
import com.mmfsin.quepreferirias.domain.models.Session
import com.mmfsin.quepreferirias.domain.usecases.CheckIfAlreadyCommentVotedUseCase
import com.mmfsin.quepreferirias.domain.usecases.GetDilemmaCommentsUseCase
import com.mmfsin.quepreferirias.domain.usecases.GetSessionUseCase
import com.mmfsin.quepreferirias.domain.usecases.InitiatedSessionUseCase
import com.mmfsin.quepreferirias.domain.usecases.SendDilemmaCommentUseCase
import com.mmfsin.quepreferirias.domain.usecases.VoteDilemmaCommentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CommentsViewModel @Inject constructor(
    private val initiatedSessionUseCase: InitiatedSessionUseCase,
    private val getSessionUseCase: GetSessionUseCase,
    private val getDilemmaCommentsUseCase: GetDilemmaCommentsUseCase,
    private val setDilemmaCommentUseCase: SendDilemmaCommentUseCase,
    private val checkIfAlreadyCommentVotedUseCase: CheckIfAlreadyCommentVotedUseCase,
    private val voteDilemmaCommentUseCase: VoteDilemmaCommentUseCase
) : BaseViewModel<CommentsEvent>() {

    fun checkSessionInitiated() {
        executeUseCase(
            { initiatedSessionUseCase.execute() },
            { result -> _event.value = CommentsEvent.InitiatedSession(result) },
            { _event.value = CommentsEvent.SWW }
        )
    }

    fun getUserData() {
        executeUseCase(
            { getSessionUseCase.execute() },
            { result -> _event.value = result?.let { CommentsEvent.UserData(it) } },
            { _event.value = CommentsEvent.SWW }
        )
    }

    fun getComments() {
        executeUseCase(
            {
                getDilemmaCommentsUseCase.execute(
                    GetDilemmaCommentsUseCase.Params("",isInitialLoad = false)
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
            { result ->
                _event.value = if (result) CommentsEvent.CommentSentResult
                else CommentsEvent.SWW
            },
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
                checkIfAlreadyCommentVotedUseCase.execute(
                    CheckIfAlreadyCommentVotedUseCase.Params(commentId, vote)
                )
            },
            { result ->
                if (result.hasVotedTheSame) _event.value = CommentsEvent.CommentAlreadyVoted
                else voteDilemmaCommentFlow(dilemmaId, commentId, vote, likes, position, result)
            },
            { _event.value = CommentsEvent.SWW }
        )
    }

    private fun voteDilemmaCommentFlow(
        dilemmaId: String,
        commentId: String,
        vote: CommentVote,
        likes: Long,
        position: Int,
        commentData: CommentAlreadyVoted
    ) {
        executeUseCase(
            {
                voteDilemmaCommentUseCase.execute(
                    VoteDilemmaCommentUseCase.Params(dilemmaId, commentId, vote, likes, commentData)
                )
            },
            {
                _event.value =
                    CommentsEvent.CommentVotedResult(vote, position, commentData.alreadyVoted)
            },
            { _event.value = CommentsEvent.SWW }
        )
    }
}