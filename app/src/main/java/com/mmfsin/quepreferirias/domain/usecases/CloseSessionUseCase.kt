package com.mmfsin.quepreferirias.domain.usecases

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mmfsin.quepreferirias.base.BaseUseCase
import com.mmfsin.quepreferirias.domain.interfaces.ISessionRepository
import com.mmfsin.quepreferirias.utils.SESSION
import com.mmfsin.quepreferirias.utils.SESSION_INITIATED
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class CloseSessionUseCase @Inject constructor(
    @ApplicationContext val context: Context,
    private val sessionRepository: ISessionRepository
) : BaseUseCase<CloseSessionUseCase.Params, Unit>() {

    override suspend fun execute(params: Params) {
        /** Google */
        params.googleClient?.signOut()

        /** Firebase */
        Firebase.auth.signOut()
        deleteData()
    }

    private fun deleteData() {
        /** Shared prefs */
        val session = context.getSharedPreferences(SESSION, MODE_PRIVATE)
        session.edit().apply() {
            putBoolean(SESSION_INITIATED, false)
            apply()
        }

        /** Realm */
        sessionRepository.deleteSession()
    }

    data class Params(
        val googleClient: GoogleSignInClient?
    )
}