package com.mmfsin.quepreferirias.presentation.main

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.mmfsin.quepreferirias.R
import com.mmfsin.quepreferirias.databinding.ActivityMainBinding
import com.mmfsin.quepreferirias.presentation.sendquestions.SendQuestionsFragment
import com.mmfsin.quepreferirias.utils.showErrorDialog
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    private var mInterstitialAd: InterstitialAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        observe()
        setNavigationDrawer()
        setDefaultBackground()

        /** */
        openDrawer()
    }

    private fun observe() {
        viewModel.event.observe(this) { event ->
            when (event) {
                is MainEvent.GoogleClient -> {
                    val client = event.user
                    client?.let {
                        val signInIntent: Intent = client.signInIntent
                        resultLauncher.launch(signInIntent)
                    }
                }
                is MainEvent.SWW -> showErrorDialog() { finish() }
            }
        }
    }

    private var resultLauncher = registerForActivityResult(StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val acct = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            val account = acct.getResult(ApiException::class.java)
        }
    }

    private fun setNavigationDrawer() {
        binding.apply {
            navigationView.setNavigationItemSelectedListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.nav_login -> viewModel.doLogin()
                    R.id.nav_send_questions -> showSendQuestions()
//                    R.id.nav_item2 -> {}
                }
                drawerLayout.closeDrawers()
                true
            }
        }
    }

//    private fun setListeners() {
//        binding.apply {
//            llMoreApps.setOnClickListener {
//                startActivity(Intent(ACTION_VIEW, Uri.parse(getString(R.string.sq_mmfsin_url))))
//            }
//        }
//    }

    fun openDrawer() = binding.drawerLayout.openDrawer(binding.navigationView)

    private fun setDefaultBackground() {
        binding.apply {
            adView.visibility = View.GONE
            val animationDrawable = clMain.background as AnimationDrawable
            animationDrawable.setEnterFadeDuration(6000)
            animationDrawable.setExitFadeDuration(6000)
            animationDrawable.start()
        }
        loadInterstitial(AdRequest.Builder().build())
    }

    fun showBanner() {
        val adRequest = AdRequest.Builder().build()
        binding.adView.apply {
            loadAd(adRequest)
            visibility = View.VISIBLE
        }
    }

    fun showInterstitial() {
        mInterstitialAd?.let { ad ->
            ad.show(this)
            loadInterstitial(AdRequest.Builder().build())
        }
    }

    private fun loadInterstitial(adRequest: AdRequest) {
        InterstitialAd.load(this,
            getString(R.string.insterstitial),
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    mInterstitialAd = null
                    loadInterstitial(AdRequest.Builder().build())
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    mInterstitialAd = interstitialAd
                }
            })
    }

    private fun showSendQuestions() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_send_questions, SendQuestionsFragment()).addToBackStack(null)
            .commit()
    }
}