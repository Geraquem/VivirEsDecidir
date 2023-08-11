package com.mmfsin.quepreferirias.presentation.root

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.mmfsin.quepreferirias.databinding.ActivityRootBinding
import com.mmfsin.quepreferirias.presentation.models.DrawerFlow.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RootActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRootBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {

    }

    private fun observe() {
//        viewModel.event.observe(this) { event ->
//            when (event) {
//                is MainEvent.DrawerFlowDirection -> {
//                    val hasSession = event.result.first
//                    if (!hasSession) {
//                        resultLauncher.launch(Intent(this@RootActivity, LoginActivity::class.java))
//                    } else {
//                        Toast.makeText(
//                            applicationContext, event.result.second.name, Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                }
//
//                is MainEvent.SWW -> showErrorDialog() { finish() }
//            }
//        }
    }
}