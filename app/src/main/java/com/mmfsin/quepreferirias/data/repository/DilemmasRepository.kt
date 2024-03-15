package com.mmfsin.quepreferirias.data.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mmfsin.quepreferirias.data.mappers.toCommentList
import com.mmfsin.quepreferirias.data.mappers.toDilemmaFavList
import com.mmfsin.quepreferirias.data.mappers.toSession
import com.mmfsin.quepreferirias.data.models.CommentDTO
import com.mmfsin.quepreferirias.data.models.DilemmaFavDTO
import com.mmfsin.quepreferirias.data.models.SessionDTO
import com.mmfsin.quepreferirias.domain.interfaces.IDilemmasRepository
import com.mmfsin.quepreferirias.domain.interfaces.IRealmDatabase
import com.mmfsin.quepreferirias.domain.models.Comment
import com.mmfsin.quepreferirias.domain.models.CommentVote
import com.mmfsin.quepreferirias.domain.models.CommentVote.VOTE_DOWN
import com.mmfsin.quepreferirias.domain.models.CommentVote.VOTE_UP
import com.mmfsin.quepreferirias.domain.models.Dilemma
import com.mmfsin.quepreferirias.domain.models.DilemmaFav
import com.mmfsin.quepreferirias.domain.models.Session
import com.mmfsin.quepreferirias.utils.COMMENTS
import com.mmfsin.quepreferirias.utils.COMMENT_LIKES
import com.mmfsin.quepreferirias.utils.CREATOR_NAME
import com.mmfsin.quepreferirias.utils.DILEMMAS
import com.mmfsin.quepreferirias.utils.SAVED_DILEMMAS
import com.mmfsin.quepreferirias.utils.SESSION
import com.mmfsin.quepreferirias.utils.TXT_BOTTOM
import com.mmfsin.quepreferirias.utils.TXT_TOP
import com.mmfsin.quepreferirias.utils.UPDATE_SAVED_DATA
import com.mmfsin.quepreferirias.utils.USERS
import com.mmfsin.quepreferirias.utils.VOTES_NO
import com.mmfsin.quepreferirias.utils.VOTES_YES
import dagger.hilt.android.qualifiers.ApplicationContext
import io.realm.kotlin.where
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.CountDownLatch
import javax.inject.Inject

class DilemmasRepository @Inject constructor(
    @ApplicationContext val context: Context,
    private val realmDatabase: IRealmDatabase
) : IDilemmasRepository {

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

    override suspend fun sendDilemmaComment(dilemmaId: String, comment: CommentDTO): Boolean {
        val latch = CountDownLatch(1)
        var result = false
        Firebase.firestore.collection(DILEMMAS).document(dilemmaId).collection(COMMENTS)
            .document(comment.commentId).set(comment, SetOptions.merge())
            .addOnCompleteListener {
                result = it.isSuccessful
                realmDatabase.addObject { comment }
                latch.countDown()
            }
        withContext(Dispatchers.IO) {
            latch.await()
        }
        return result
    }

    override suspend fun voteDilemmaComment(
        dilemmaId: String,
        commentId: String,
        likes: Long,
        vote: CommentVote
    ) {
        val documentReference = Firebase.firestore.collection(DILEMMAS).document(dilemmaId)
            .collection(COMMENTS).document(commentId)
        val updatedLikes = hashMapOf<String, Any>(COMMENT_LIKES to likes)
        documentReference.update(updatedLikes)

        val comment = realmDatabase.getObjectsFromRealm {
            where<CommentDTO>().equalTo("commentId", commentId).findAll()
        }.first()
        comment.likes = likes
        when (vote) {
            VOTE_UP -> {
                comment.votedUp = true
                comment.votedDown = false
            }

            VOTE_DOWN -> {
                comment.votedUp = false
                comment.votedDown = true
            }
        }
        realmDatabase.addObject { comment }
    }

    private fun getSession(): Session? {
        val session = realmDatabase.getObjectsFromRealm { where<SessionDTO>().findAll() }
        return if (session.isEmpty()) null else session.first().toSession()
    }

    override suspend fun setFavDilemma(dilemma: DilemmaFavDTO) {
        val session = getSession()
        val latch = CountDownLatch(1)
        session?.let {
            Firebase.firestore.collection(USERS).document(session.id)
                .collection(SAVED_DILEMMAS).document(dilemma.dilemmaId)
                .set(dilemma, SetOptions.merge())
                .addOnCompleteListener {
                    realmDatabase.addObject { dilemma }
                    latch.countDown()
                }
            withContext(Dispatchers.IO) { latch.await() }
        }
    }

    override suspend fun getFavDilemmas(userId: String): List<DilemmaFav> {
        val session = getSession()
        val latch = CountDownLatch(1)
        return session?.let {
            val sharedPrefs = context.getSharedPreferences(SESSION, Context.MODE_PRIVATE)
            if (sharedPrefs.getBoolean(UPDATE_SAVED_DATA, true)) {
                Toast.makeText(context, "buscando en firebase", Toast.LENGTH_SHORT).show()
                val dilemmas = mutableListOf<DilemmaFavDTO>()
                Firebase.firestore.collection(USERS).document(session.id)
                    .collection(SAVED_DILEMMAS).get().addOnSuccessListener { d ->
                        for (document in d.documents) {
                            val a = 2
                        }
                        latch.countDown()
                    }.addOnFailureListener {
                        latch.countDown()
                    }
                withContext(Dispatchers.IO) { latch.await() }
                sharedPrefs.edit().apply {
                    putBoolean(UPDATE_SAVED_DATA, false)
                    apply()
                }
                dilemmas.toDilemmaFavList()
            } else {
                Toast.makeText(context, "buscando en realm", Toast.LENGTH_SHORT).show()
                val dilemmas =
                    realmDatabase.getObjectsFromRealm { where<DilemmaFavDTO>().findAll() }
                dilemmas.toDilemmaFavList()
            }
        } ?: run { emptyList() }
    }

    private fun searchFavDilemmasInFirebase(userId: String): List<DilemmaFavDTO> {
        /**  buscar si el shared prefs esta a true */
        /** una vez haya buscado, poner a false */
        /** si está a false significa que no hay nada por lo que return emptylist */
        return emptyList()
    }
}
