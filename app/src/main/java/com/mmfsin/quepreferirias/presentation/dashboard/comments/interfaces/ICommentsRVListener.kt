package com.mmfsin.quepreferirias.presentation.dashboard.comments.interfaces

import com.mmfsin.quepreferirias.domain.models.CommentReply
import com.mmfsin.quepreferirias.domain.models.CommentVote

interface ICommentsRVListener {
    fun onCommentNameClick(userId: String)
    fun openCommentMenu(commentId: String, commentText: String, userId: String)
    fun voteComment(commentId: String, vote: CommentVote, likes: Long, position: Int)
    fun respondComment(reply: CommentReply)

    fun onReplyNameClick(userId: String)
    fun openReplyMenu(reply: CommentReply)
//    fun voteReply(commentId: String, vote: CommentVote, likes: Long, position: Int)
}