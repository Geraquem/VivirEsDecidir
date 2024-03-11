package com.mmfsin.quepreferirias.data.repository

import android.util.Log
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mmfsin.quepreferirias.data.mappers.toCommentList
import com.mmfsin.quepreferirias.data.models.CommentDTO
import com.mmfsin.quepreferirias.data.models.SessionDTO
import com.mmfsin.quepreferirias.data.models.UserNameDTO
import com.mmfsin.quepreferirias.domain.interfaces.IDataRepository
import com.mmfsin.quepreferirias.domain.interfaces.IRealmDatabase
import com.mmfsin.quepreferirias.domain.models.Comment
import com.mmfsin.quepreferirias.domain.models.Dilemma
import com.mmfsin.quepreferirias.utils.COMMENTS
import com.mmfsin.quepreferirias.utils.COMMENT_LIKES
import com.mmfsin.quepreferirias.utils.CREATOR_NAME
import com.mmfsin.quepreferirias.utils.DILEMMAS
import com.mmfsin.quepreferirias.utils.TXT_BOTTOM
import com.mmfsin.quepreferirias.utils.TXT_TOP
import com.mmfsin.quepreferirias.utils.VOTES_NO
import com.mmfsin.quepreferirias.utils.VOTES_YES
import io.realm.kotlin.where
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.CountDownLatch
import javax.inject.Inject

class DataRepository @Inject constructor(
    private val realmDatabase: IRealmDatabase
) : IDataRepository {

    private val reference = Firebase.database.reference

    override suspend fun getDilemmas(): List<Dilemma> {
        val latch = CountDownLatch(1)
        val dilemmaList = mutableListOf<Dilemma>()
        val root = reference.child(DILEMMAS)
        root.get().addOnCompleteListener { dataSnapshot ->
            for (child in dataSnapshot.result.children) {
                if (child.exists()) {
                    val id = child.key ?: child.ref.key
                    val textTop = child.child(TXT_TOP).value.toString()
                    val textBottom = child.child(TXT_BOTTOM).value.toString()
                    val votesYes = child.child(VOTES_YES).childrenCount
                    val votesNo = child.child(VOTES_NO).childrenCount
                    val creator = child.child(CREATOR_NAME).value?.toString()
                    val data = Dilemma(
                        id.toString(),
                        textTop,
                        textBottom,
                        votesYes,
                        votesNo,
                        creator
                    )
                    dilemmaList.add(data)
                }
            }
            latch.countDown()
        }.addOnFailureListener { latch.countDown() }

        withContext(Dispatchers.IO)
        {
            latch.await()
        }
        return dilemmaList//.shuffled()
    }

    override suspend fun getDilemmaComments(dilemmaId: String): List<Comment> {
        val comments = mutableListOf<CommentDTO>()
        val latch = CountDownLatch(1)

        realmDatabase.deleteAllObjects(CommentDTO::class.java)
        Firebase.firestore.collection(DILEMMAS).document(dilemmaId)
            .collection(COMMENTS).get().addOnSuccessListener { d ->
                for (document in d.documents) {
                    try {
                        document.toObject(CommentDTO::class.java)?.let { comment ->
                            comments.add(comment)
                            realmDatabase.addObject { comment }
                        }
                    } catch (e: Exception) {
                        Log.e("error", "error parsing comment")
                    }
                }
                latch.countDown()
            }.addOnFailureListener {
                latch.countDown()
            }
        withContext(Dispatchers.IO) { latch.await() }
        return sortedComments(comments)
    }

    override suspend fun getDilemmaCommentFromRealm(): List<Comment> {
        val comments = realmDatabase.getObjectsFromRealm { where<CommentDTO>().findAll() }
        return sortedComments(comments)
    }

    private fun sortedComments(comments: List<CommentDTO>): List<Comment> {
        val sortedList = comments.sortedBy { it.timestamp }.reversed()
        return sortedList.toCommentList()
    }

    override suspend fun setDilemmaComment(dilemmaId: String, comment: CommentDTO): Boolean {
        val latch = CountDownLatch(1)
        var result = false
        Firebase.firestore.collection(DILEMMAS).document(dilemmaId).collection(COMMENTS)
            .document(comment.commentId).set(comment, SetOptions.merge())
            .addOnCompleteListener {
                result = it.isSuccessful
                latch.countDown()
            }
        withContext(Dispatchers.IO) {
            latch.await()
        }
        return result
    }

    override suspend fun voteDilemmaComment(dilemmaId: String, commentId: String, likes: Long) {
        val documentReference = Firebase.firestore.collection(DILEMMAS).document(dilemmaId)
            .collection(COMMENTS).document(commentId)

        val updatedLikes = hashMapOf<String, Any>(
            COMMENT_LIKES to likes
        )

        documentReference.update(updatedLikes)
            .addOnSuccessListener {
                println("Nombre del usuario actualizado exitosamente.")
            }
            .addOnFailureListener { e ->
                println("Error al actualizar el nombre del usuario: $e")
            }
    }
}
