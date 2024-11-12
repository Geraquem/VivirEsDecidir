package com.mmfsin.quepreferirias.domain.interfaces

import com.mmfsin.quepreferirias.data.models.CommentDTO
import com.mmfsin.quepreferirias.data.models.CommentReplyDTO
import com.mmfsin.quepreferirias.domain.models.Comment
import com.mmfsin.quepreferirias.domain.models.CommentAlreadyVoted
import com.mmfsin.quepreferirias.domain.models.CommentReply
import com.mmfsin.quepreferirias.domain.models.CommentVote

interface ICommentsRepository {

    suspend fun getComments(dataId: String, root: String): List<Comment>
    suspend fun sendComment(dataId: String, root: String, comment: CommentDTO): Comment?
    suspend fun respondComment(
        dataId: String,
        root: String,
        commentId: String,
        reply: CommentReplyDTO
    ): CommentReply?

    suspend fun deleteComment(dataId: String, root: String, commentId: String): Boolean
    suspend fun alreadyCommentVoted(commentId: String, vote: CommentVote): CommentAlreadyVoted
    suspend fun voteComment(
        dataId: String,
        root: String,
        commentId: String,
        likes: Long,
        vote: CommentVote
    )

    suspend fun reportComment(dataId: String, comment: Comment)
}