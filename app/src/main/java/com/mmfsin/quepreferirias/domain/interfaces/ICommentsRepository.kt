package com.mmfsin.quepreferirias.domain.interfaces

import com.mmfsin.quepreferirias.data.models.CommentDTO
import com.mmfsin.quepreferirias.domain.models.Comment
import com.mmfsin.quepreferirias.domain.models.CommentAlreadyVoted
import com.mmfsin.quepreferirias.domain.models.CommentVote

interface ICommentsRepository {

    suspend fun getDilemmaComments(dilemmaId: String): List<Comment>
    suspend fun sendDilemmaComment(dilemmaId: String, comment: CommentDTO): Comment?
    suspend fun deleteDilemmaComment(dilemmaId: String, commentId: String): Boolean
    suspend fun alreadyCommentVoted(commentId: String, vote: CommentVote): CommentAlreadyVoted
    suspend fun voteDilemmaComment(
        dilemmaId: String,
        commentId: String,
        likes: Long,
        vote: CommentVote
    )

    suspend fun reportComment(dataId: String, comment: Comment)
}