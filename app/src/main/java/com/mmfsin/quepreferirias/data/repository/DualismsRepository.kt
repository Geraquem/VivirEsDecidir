package com.mmfsin.quepreferirias.data.repository

import android.content.Context
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mmfsin.quepreferirias.data.models.SendDualismDTO
import com.mmfsin.quepreferirias.domain.interfaces.IDualismsRepository
import com.mmfsin.quepreferirias.domain.interfaces.IRealmDatabase
import com.mmfsin.quepreferirias.utils.DUALISMS
import com.mmfsin.quepreferirias.utils.DUALISMS_SENT
import com.mmfsin.quepreferirias.utils.REPORTED
import com.mmfsin.quepreferirias.utils.USERS
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.CountDownLatch
import javax.inject.Inject

class DualismsRepository @Inject constructor(
    @ApplicationContext val context: Context,
    private val realmDatabase: IRealmDatabase
) : IDualismsRepository {

    private val reference = Firebase.database.reference

    override suspend fun sendDualism(dualism: SendDualismDTO) {
        val latch = CountDownLatch(3)

        /** Set in User Dualisms */
        Firebase.firestore.collection(USERS).document(dualism.creatorId)
            .collection(DUALISMS_SENT).document(dualism.dualismId)
            .set(dualism, SetOptions.merge())
            .addOnCompleteListener {
                realmDatabase.addObject { dualism }
                latch.countDown()
            }


        /** Set in total dualisms */
        Firebase.firestore.collection(DUALISMS).document(dualism.dualismId)
            .set(dualism, SetOptions.merge())
            .addOnCompleteListener {
                latch.countDown()
            }

        /** Set in Realtime for votes */
        val root = reference.child(DUALISMS).child(dualism.dualismId)
        root.setValue(dualism.dualismId).addOnSuccessListener {
            latch.countDown()
        }

        withContext(Dispatchers.IO) { latch.await() }
    }

    override suspend fun reportDualism(dualismId: String) {

    }
}