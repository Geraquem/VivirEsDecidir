package com.mmfsin.quepreferirias.domain.interfaces

import com.mmfsin.quepreferirias.data.models.CommentDTO
import com.mmfsin.quepreferirias.data.models.DilemmaFavDTO
import com.mmfsin.quepreferirias.data.models.DilemmaVotedDTO
import com.mmfsin.quepreferirias.data.models.SendDilemmaDTO
import com.mmfsin.quepreferirias.domain.models.Comment
import com.mmfsin.quepreferirias.domain.models.CommentAlreadyVoted
import com.mmfsin.quepreferirias.domain.models.CommentVote
import com.mmfsin.quepreferirias.domain.models.Dilemma
import com.mmfsin.quepreferirias.domain.models.DilemmaFav
import com.mmfsin.quepreferirias.domain.models.DilemmaVotes
import com.mmfsin.quepreferirias.domain.models.SendDilemma

interface ICommentsRepository {

    suspend fun getDilemmaComments(dilemmaId: String): List<Comment>
    suspend fun sendDilemmaComment(dilemmaId: String, comment: CommentDTO): Boolean
    suspend fun alreadyCommentVoted(commentId: String, vote: CommentVote): CommentAlreadyVoted
    suspend fun voteDilemmaComment(
        dilemmaId: String,
        commentId: String,
        likes: Long,
        vote: CommentVote
    )
}