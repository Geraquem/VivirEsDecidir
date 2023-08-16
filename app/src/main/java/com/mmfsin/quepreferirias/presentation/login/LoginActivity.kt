package com.mmfsin.quepreferirias.presentation.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.mmfsin.quepreferirias.databinding.ActivityLoginBinding
import com.mmfsin.quepreferirias.utils.showErrorDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        setListeners()
        observe()
    }

    private fun setListeners() {
        binding.apply {
            btnGoogle.setOnClickListener { viewModel.googleLogin() }
        }
    }

    private fun observe() {
        viewModel.event.observe(this) { event ->
            when (event) {
                is LoginEvent.GoogleClient -> {
                    val client = event.user
                    client?.let {
                        val signInIntent: Intent = client.signInIntent
                        resultLauncher.launch(signInIntent)
                    }
                }
                is LoginEvent.SessionSaved -> {
                    setResult(RESULT_OK)
                    finish()
                }
                is LoginEvent.SWW -> error()
            }
        }
    }

    private var resultLauncher = registerForActivityResult(StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val acct = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = acct.getResult(ApiException::class.java)
                account?.let {
                    viewModel.saveSession(it.id ?: "", it.email ?: "", account.displayName ?: "")
                } ?: run { error() }
            } catch (e: ApiException) {
                error()
            }
        } else error()
    }

    private fun error() = showErrorDialog() { finish() }
}