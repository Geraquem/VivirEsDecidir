package com.mmfsin.quepreferirias.domain.usecases

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mmfsin.quepreferirias.base.BaseUseCase
import com.mmfsin.quepreferirias.domain.interfaces.IUserRepository
import com.mmfsin.quepreferirias.utils.SERVER_SAVED_DILEMMAS
import com.mmfsin.quepreferirias.utils.SERVER_SAVED_DUALISMS
import com.mmfsin.quepreferirias.utils.SERVER_SENT_DILEMMAS
import com.mmfsin.quepreferirias.utils.SERVER_SENT_DUALISMS
import com.mmfsin.quepreferirias.utils.SERVER_USER_DATA
import com.mmfsin.quepreferirias.utils.SESSION
import com.mmfsin.quepreferirias.utils.SESSION_INITIATED
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    @ApplicationContext val context: Context,
    private val repository: IUserRepository
) : BaseUseCase<LogoutUseCase.Params, Unit>() {

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
            putBoolean(SERVER_USER_DATA, false)

            putBoolean(SERVER_SAVED_DILEMMAS, false)
            putBoolean(SERVER_SENT_DILEMMAS, false)

            putBoolean(SERVER_SAVED_DUALISMS, false)
            putBoolean(SERVER_SENT_DUALISMS, false)
            apply()
        }

        /** Realm */
        repository.deleteSession()
    }

    data class Params(
        val googleClient: GoogleSignInClient?
    )
}