package com.mmfsin.quepreferirias.domain.usecases

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.mmfsin.quepreferirias.base.BaseUseCase
import com.mmfsin.quepreferirias.domain.interfaces.ISessionRepository
import com.mmfsin.quepreferirias.domain.models.Session
import com.mmfsin.quepreferirias.utils.SESSION
import com.mmfsin.quepreferirias.utils.SESSION_INITIATED
import com.mmfsin.quepreferirias.utils.UPDATE_SAVED_DATA
import com.mmfsin.quepreferirias.utils.UPDATE_SENT_DATA
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.CountDownLatch
import javax.inject.Inject

class SaveSessionUseCase @Inject constructor(
    @ApplicationContext val context: Context,
    private val sessionRepository: ISessionRepository
) : BaseUseCase<SaveSessionUseCase.Params, Unit>() {

    /** Save in shared prefs, Firebase and Realm */
    override suspend fun execute(params: Params) {
        /** Shared prefs */
        val session = context.getSharedPreferences(SESSION, MODE_PRIVATE)
        session.edit().apply() {
            putBoolean(SESSION_INITIATED, true)
            putBoolean(UPDATE_SAVED_DATA, true)
            putBoolean(UPDATE_SENT_DATA, true)
            apply()
        }

        /** Firebase ERRORRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR*/
//        val credential = GoogleAuthProvider.getCredential(params.id, null)
//        val latch = CountDownLatch(1)
//        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
//            if (it.isSuccessful){
//                val a = 2
//                latch.countDown()
//            }else{
//                val a = 2
//                latch.countDown()
//            }
//        }
//
//        withContext(Dispatchers.IO) {
//            latch.await()
//        }

        /** Realm */
        sessionRepository.saveSession(params.session)
    }

    data class Params(
        val id: String,
        val session: Session
    )
}