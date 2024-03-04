package com.mmfsin.quepreferirias.data.repository

import android.util.Log
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mmfsin.quepreferirias.data.mappers.toCommentList
import com.mmfsin.quepreferirias.data.models.CommentDTO
import com.mmfsin.quepreferirias.domain.interfaces.IDataRepository
import com.mmfsin.quepreferirias.domain.interfaces.IRealmDatabase
import com.mmfsin.quepreferirias.domain.models.Comment
import com.mmfsin.quepreferirias.domain.models.Dilemma
import com.mmfsin.quepreferirias.utils.COMMENTS
import com.mmfsin.quepreferirias.utils.DILEMMAS
import com.mmfsin.quepreferirias.utils.CREATOR_NAME
import com.mmfsin.quepreferirias.utils.TXT_BOTTOM
import com.mmfsin.quepreferirias.utils.TXT_TOP
import com.mmfsin.quepreferirias.utils.VOTES_NO
import com.mmfsin.quepreferirias.utils.VOTES_YES
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
        Firebase.firestore.collection(DILEMMAS).document(dilemmaId)
            .collection(COMMENTS).get().addOnSuccessListener { d ->
                for (document in d.documents) {
                    try {
                        document.toObject(CommentDTO::class.java)?.let { comment ->
                            comments.add(comment)
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
        return comments.toCommentList()
    }
}
