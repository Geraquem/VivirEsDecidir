package com.mmfsin.quepreferirias.presentation.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.mmfsin.quepreferirias.R
import com.mmfsin.quepreferirias.databinding.ActivityLoginBinding
import com.mmfsin.quepreferirias.domain.models.Session
import com.mmfsin.quepreferirias.presentation.login.adapter.LoginAdapter
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
        setUI()
        setListeners()
        observe()
    }

    private fun setUI() {
        val arrayList = resources.getStringArray(R.array.login_txt_list)
        binding.rvLogin.apply {
            layoutManager = LinearLayoutManager(this@LoginActivity)
            adapter = LoginAdapter(arrayList.toList())
        }
    }

    private fun setListeners() {
        binding.apply {
            toolbar.ivBack.setOnClickListener { finish() }
            btnLogin.btnGoogle.setOnClickListener { viewModel.googleLogin() }
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
                    val session = Session(
                        id = it.id ?: "",
                        imageUrl = it.photoUrl.toString(),
                        email = it.email ?: "",
                        name = it.givenName ?: "",
                        fullName = it.displayName ?: ""
                    )
                    viewModel.saveSession(session)
                } ?: run { error() }
            } catch (e: ApiException) {
                error()
            }
        } else error()
    }

    private fun error() = showErrorDialog() { finish() }
}