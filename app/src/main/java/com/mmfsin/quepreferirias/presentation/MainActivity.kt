package com.mmfsin.quepreferirias.presentation

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.mmfsin.quepreferirias.R
import com.mmfsin.quepreferirias.databinding.ActivityMainBinding
import com.mmfsin.quepreferirias.presentation.sendquestions.SendQuestionsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var mInterstitialAd: InterstitialAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        setNavigationDrawer()
        binding.apply {
            adView.visibility = View.GONE
            val animationDrawable = clMain.background as AnimationDrawable
            animationDrawable.setEnterFadeDuration(6000)
            animationDrawable.setExitFadeDuration(6000)
            animationDrawable.start()
        }
        loadInterstitial(AdRequest.Builder().build())
    }

    private fun setNavigationDrawer() {
        binding.apply {
            navigationView.setNavigationItemSelectedListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.nav_item1 -> {}
                    R.id.nav_item2 -> {}
                }
                drawerLayout.closeDrawers()
                true
            }
        }
    }

    fun openDrawer() = binding.drawerLayout.openDrawer(binding.navigationView)

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

    fun showSendQuestions() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_send_questions, SendQuestionsFragment()).addToBackStack(null)
            .commit()
    }


}