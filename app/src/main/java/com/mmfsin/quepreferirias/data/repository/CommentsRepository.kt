package com.mmfsin.quepreferirias.data.repository

import android.content.Context
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mmfsin.quepreferirias.data.mappers.toComment
import com.mmfsin.quepreferirias.data.mappers.toCommentList
import com.mmfsin.quepreferirias.data.mappers.toCommentReply
import com.mmfsin.quepreferirias.data.models.CommentDTO
import com.mmfsin.quepreferirias.data.models.CommentReplyDTO
import com.mmfsin.quepreferirias.data.models.CommentVotedDTO
import com.mmfsin.quepreferirias.domain.interfaces.ICommentsRepository
import com.mmfsin.quepreferirias.domain.interfaces.IRealmDatabase
import com.mmfsin.quepreferirias.domain.models.Comment
import com.mmfsin.quepreferirias.domain.models.CommentAlreadyVoted
import com.mmfsin.quepreferirias.domain.models.CommentReply
import com.mmfsin.quepreferirias.domain.models.CommentVote
import com.mmfsin.quepreferirias.domain.models.CommentVote.VOTE_DOWN
import com.mmfsin.quepreferirias.domain.models.CommentVote.VOTE_UP
import com.mmfsin.quepreferirias.utils.COMMENTS
import com.mmfsin.quepreferirias.utils.COMMENT_ID
import com.mmfsin.quepreferirias.utils.COMMENT_LIKES
import com.mmfsin.quepreferirias.utils.COMMENT_REPLIES
import com.mmfsin.quepreferirias.utils.REPORTED
import com.mmfsin.quepreferirias.utils.TIMESTAMP
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.CountDownLatch
import javax.inject.Inject

class CommentsRepository @Inject constructor(
    @ApplicationContext val context: Context,
    private val realmDatabase: IRealmDatabase
) : ICommentsRepository {

    private var lastCommentVisible: DocumentSnapshot? = null
    private var lastCommentLikes: Long? = null
    private var lastCommentTimestamp: Long? = null

    override suspend fun getComments(dataId: String, root: String): List<Comment> {
        val latch = CountDownLatch(1)
        var result = mutableListOf<CommentDTO>()

        val commentsRef =
            Firebase.firestore.collection(root).document(dataId).collection(COMMENTS)

        val query = if (lastCommentVisible != null) {

            commentsRef
                .orderBy(COMMENT_LIKES, Query.Direction.DESCENDING)
                .orderBy(TIMESTAMP, Query.Direction.DESCENDING)
                .startAfter(lastCommentLikes, lastCommentTimestamp)
                .limit(5)
        } else {
            commentsRef
                .orderBy(COMMENT_LIKES, Query.Direction.DESCENDING)
                .orderBy(TIMESTAMP, Query.Direction.DESCENDING)
                .limit(5)
        }

        query.get()
            .addOnSuccessListener { snapshot ->
                if (!snapshot.isEmpty) {
                    val comments = snapshot.toObjects(CommentDTO::class.java)
                    lastCommentVisible = snapshot.documents.lastOrNull()
                    lastCommentLikes = lastCommentVisible?.getLong(COMMENT_LIKES)
                    lastCommentTimestamp = lastCommentVisible?.getLong(TIMESTAMP)
                    result = comments
                }
                latch.countDown()
            }
            .addOnFailureListener {
                latch.countDown()
            }

        withContext(Dispatchers.IO) { latch.await() }
        return result.toCommentList()
    }

    override suspend fun sendComment(
        dataId: String,
        root: String,
        comment: CommentDTO
    ): Comment? {
        val latch = CountDownLatch(1)
        var result: Comment? = null
        Firebase.firestore.collection(root).document(dataId).collection(COMMENTS)
            .document(comment.commentId).set(comment, SetOptions.merge())
            .addOnCompleteListener {
                if (it.isSuccessful) result = comment.toComment()
                latch.countDown()
            }
        withContext(Dispatchers.IO) {
            latch.await()
        }
        return result
    }

    override suspend fun respondComment(
        dataId: String,
        root: String,
        reply: CommentReplyDTO
    ): CommentReply? {
        val latch = CountDownLatch(1)
        var result: CommentReply? = null
        Firebase.firestore.collection(root).document(dataId).collection(COMMENTS)
            .document(reply.commentId).update(COMMENT_REPLIES, FieldValue.arrayUnion(reply))
            .addOnCompleteListener {
                if (it.isSuccessful) result = reply.toCommentReply()
                latch.countDown()
            }
        withContext(Dispatchers.IO) {
            latch.await()
        }
        return result
    }

    override suspend fun deleteComment(dataId: String, root: String, commentId: String): Boolean {
        val latch = CountDownLatch(1)
        var result = false
        Firebase.firestore.collection(root).document(dataId).collection(COMMENTS)
            .document(commentId).delete().addOnCompleteListener {
                result = it.isSuccessful
                latch.countDown()
            }
        withContext(Dispatchers.IO) {
            latch.await()
        }
        return result
    }

    override suspend fun deleteCommentReply(
        dataId: String,
        root: String,
        commentId: String,
        replyId: String
    ): Boolean {
        val latch = CountDownLatch(1)
        var result = false

        val rootA = Firebase.firestore.collection(root).document(dataId).collection(COMMENTS)
            .document(commentId)
        rootA.get().addOnSuccessListener { document ->
            if (document.exists()) {
                val comment = document.toObject(CommentDTO::class.java)
                comment?.let { c ->
                    val updated = c.replies.filterNot { it.replyId == replyId }
                    rootA.update(COMMENT_REPLIES, updated)
                        .addOnCompleteListener {
                            result = it.isSuccessful
                            latch.countDown()
                        }
                }
            }
        }
        withContext(Dispatchers.IO) {
            latch.await()
        }
        return result
    }

    override suspend fun alreadyCommentVoted(
        commentId: String,
        vote: CommentVote
    ): CommentAlreadyVoted {
        val voted =
            realmDatabase.getObjectFromRealm(
                CommentVotedDTO::class.java,
                COMMENT_ID,
                commentId
            )
        val alreadyVoted = (voted != null)
        val hasVotedTheSame = voted?.let {
            /** ok sólo si el voto que tengo guardado es distinto del voto actual */
            if (it.votedUp && vote == VOTE_UP) true
            else !it.votedUp && vote == VOTE_DOWN
        } ?: run { false }

        return CommentAlreadyVoted(alreadyVoted, hasVotedTheSame)
    }

    override suspend fun voteComment(
        dataId: String,
        root: String,
        commentId: String,
        likes: Long,
        vote: CommentVote
    ) {
        val documentReference =
            Firebase.firestore.collection(root).document(dataId)
                .collection(COMMENTS).document(commentId)
        val updatedLikes = hashMapOf<String, Any>(COMMENT_LIKES to likes)
        documentReference.update(updatedLikes)

        /** save voted comment to not vote again */
        val votedUp = vote == VOTE_UP
        realmDatabase.addObject {
            CommentVotedDTO(
                commentId = commentId,
                votedUp = votedUp
            )
        }
    }

    override suspend fun reportComment(dataId: String, comment: Comment) {
        val latch = CountDownLatch(1)
        Firebase.firestore.collection(REPORTED)
            .document(COMMENTS).collection(comment.commentId)
            .document(comment.commentId).set(comment)
            .addOnCompleteListener {
                latch.countDown()
            }
        withContext(Dispatchers.IO) { latch.await() }
    }
}
