package com.mmfsin.quepreferirias.presentation.dashboard.dilemmas.comments

import com.mmfsin.quepreferirias.base.BaseViewModel
import com.mmfsin.quepreferirias.domain.models.Comment
import com.mmfsin.quepreferirias.domain.models.CommentAlreadyVoted
import com.mmfsin.quepreferirias.domain.models.CommentVote
import com.mmfsin.quepreferirias.domain.usecases.CheckIfAlreadyCommentVotedUseCase
import com.mmfsin.quepreferirias.domain.usecases.GetDilemmaCommentsUseCase
import com.mmfsin.quepreferirias.domain.usecases.InitiatedSessionUseCase
import com.mmfsin.quepreferirias.domain.usecases.ReportCommentUseCase
import com.mmfsin.quepreferirias.domain.usecases.VoteDilemmaCommentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CommentsViewModel @Inject constructor(
    private val initiatedSessionUseCase: InitiatedSessionUseCase,
    private val getDilemmaCommentsUseCase: GetDilemmaCommentsUseCase,
    private val checkIfAlreadyCommentVotedUseCase: CheckIfAlreadyCommentVotedUseCase,
    private val voteDilemmaCommentUseCase: VoteDilemmaCommentUseCase,
    private val reportCommentUseCase: ReportCommentUseCase
) : BaseViewModel<CommentsEvent>() {

    fun checkSessionInitiated() {
        executeUseCase(
            { initiatedSessionUseCase.execute() },
            { result -> _event.value = CommentsEvent.CheckIfSession(result) },
            { _event.value = CommentsEvent.SWW }
        )
    }

    fun getComments(dilemmaId: String) {
        executeUseCase(
            { getDilemmaCommentsUseCase.execute(GetDilemmaCommentsUseCase.Params(dilemmaId)) },
            { result -> _event.value = CommentsEvent.Comments(result) },
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

    fun reportComment(dilemmaId: String, comment: Comment) {
        executeUseCase(
            {
                reportCommentUseCase.execute(
                    ReportCommentUseCase.Params(dilemmaId, comment)
                )
            },
            { _event.value = CommentsEvent.CommentReported },
            { _event.value = CommentsEvent.SWW }
        )
    }
}