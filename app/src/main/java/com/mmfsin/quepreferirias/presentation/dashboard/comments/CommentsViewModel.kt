package com.mmfsin.quepreferirias.presentation.dashboard.comments

import com.mmfsin.quepreferirias.base.BaseViewModel
import com.mmfsin.quepreferirias.domain.models.Comment
import com.mmfsin.quepreferirias.domain.models.CommentAlreadyVoted
import com.mmfsin.quepreferirias.domain.models.CommentVote
import com.mmfsin.quepreferirias.domain.models.DataToRespondComment
import com.mmfsin.quepreferirias.domain.usecases.CheckIfAlreadyCommentVotedUseCase
import com.mmfsin.quepreferirias.domain.usecases.GetDataCommentsUseCase
import com.mmfsin.quepreferirias.domain.usecases.GetSessionUseCase
import com.mmfsin.quepreferirias.domain.usecases.InitiatedSessionUseCase
import com.mmfsin.quepreferirias.domain.usecases.ReportCommentUseCase
import com.mmfsin.quepreferirias.domain.usecases.VoteDataCommentUseCase
import com.mmfsin.quepreferirias.presentation.models.DashboardType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CommentsViewModel @Inject constructor(
    private val initiatedSessionUseCase: InitiatedSessionUseCase,
    private val getSessionUseCase: GetSessionUseCase,
    private val getDataCommentsUseCase: GetDataCommentsUseCase,
    private val checkIfAlreadyCommentVotedUseCase: CheckIfAlreadyCommentVotedUseCase,
    private val voteDataCommentUseCase: VoteDataCommentUseCase,
    private val reportCommentUseCase: ReportCommentUseCase
) : BaseViewModel<CommentsEvent>() {

    fun checkSessionInitiated() {
        executeUseCase(
            { initiatedSessionUseCase.execute() },
            { result -> _event.value = CommentsEvent.CheckIfSession(result) },
            { _event.value = CommentsEvent.SWW }
        )
    }

    fun getComments(dataId: String, type: DashboardType) {
        executeUseCase(
            { getDataCommentsUseCase.execute(GetDataCommentsUseCase.Params(dataId, type)) },
            { result -> _event.value = CommentsEvent.Comments(result) },
            { _event.value = CommentsEvent.SWW }
        )
    }

    fun voteComment(
        dataId: String,
        type: DashboardType,
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
                else voteDilemmaCommentFlow(dataId, type, commentId, vote, likes, position, result)
            },
            { _event.value = CommentsEvent.SWW }
        )
    }

    private fun voteDilemmaCommentFlow(
        dataId: String,
        type: DashboardType,
        commentId: String,
        vote: CommentVote,
        likes: Long,
        position: Int,
        commentData: CommentAlreadyVoted
    ) {
        executeUseCase(
            {
                voteDataCommentUseCase.execute(
                    VoteDataCommentUseCase.Params(
                        dataId,
                        type,
                        commentId,
                        vote,
                        likes,
                        commentData
                    )
                )
            },
            {
                _event.value =
                    CommentsEvent.CommentVotedResult(vote, position, commentData.alreadyVoted)
            },
            { _event.value = CommentsEvent.SWW }
        )
    }

    fun getSessionToRespondComment(data: DataToRespondComment) {
        executeUseCase(
            { getSessionUseCase.execute() },
            { result -> _event.value = CommentsEvent.GetSessionToRespondComment(result, data) },
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