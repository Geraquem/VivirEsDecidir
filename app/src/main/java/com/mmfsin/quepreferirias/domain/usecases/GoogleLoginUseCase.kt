package com.mmfsin.quepreferirias.domain.usecases

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.mmfsin.quepreferirias.R
import com.mmfsin.quepreferirias.base.BaseUseCaseNoParams
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GoogleLoginUseCase @Inject constructor(
    @ApplicationContext val context: Context,
) : BaseUseCaseNoParams<GoogleSignInClient>() {

    override suspend fun execute(): GoogleSignInClient {
        val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.google_oauth_token))
            .requestEmail().build()
        return GoogleSignIn.getClient(context, googleConf)
    }
}