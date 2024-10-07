package com.mmfsin.quepreferirias.presentation.dashboard.dilemmas.comments

import com.mmfsin.quepreferirias.base.BaseViewModel
import com.mmfsin.quepreferirias.domain.models.CommentAlreadyVoted
import com.mmfsin.quepreferirias.domain.models.CommentVote
import com.mmfsin.quepreferirias.domain.usecases.CheckIfAlreadyCommentVotedUseCase
import com.mmfsin.quepreferirias.domain.usecases.GetDilemmaCommentsUseCase
import com.mmfsin.quepreferirias.domain.usecases.VoteDilemmaCommentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CommentsViewModel @Inject constructor(
    private val getDilemmaCommentsUseCase: GetDilemmaCommentsUseCase,
    private val checkIfAlreadyCommentVotedUseCase: CheckIfAlreadyCommentVotedUseCase,
    private val voteDilemmaCommentUseCase: VoteDilemmaCommentUseCase
) : BaseViewModel<CommentsEvent>() {

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
}