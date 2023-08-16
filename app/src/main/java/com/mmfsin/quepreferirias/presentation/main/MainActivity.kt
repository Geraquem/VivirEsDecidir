package com.mmfsin.quepreferirias.presentation.main

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.mmfsin.quepreferirias.R
import com.mmfsin.quepreferirias.databinding.ActivityMainBinding
import com.mmfsin.quepreferirias.presentation.login.LoginActivity
import com.mmfsin.quepreferirias.presentation.models.DrawerFlow
import com.mmfsin.quepreferirias.presentation.models.DrawerFlow.*
import com.mmfsin.quepreferirias.utils.ROOT_ACTIVITY_NAV_GRAPH
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
        setListeners()
        setNavigationDrawer()
        setAds()
    }

    private fun observe() {
        viewModel.event.observe(this) { event ->
            when (event) {
                is MainEvent.DrawerFlowDirection -> {
                    val hasSession = event.result.first
                    if (hasSession) navigateDrawer(event.result.second) else loginFlow()
                }

                is MainEvent.SWW -> showErrorDialog() { finish() }
            }
        }
    }

    fun loginFlow() =
        resultLauncher.launch(Intent(this@MainActivity, LoginActivity::class.java))

    private var resultLauncher = registerForActivityResult(StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            Toast.makeText(applicationContext, "BIENVENIDO/A", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateDrawer(flow: DrawerFlow) {
        var navGraph: Int? = null
        when (flow) {
            PROFILE -> navGraph = R.navigation.nav_graph_profile
            SAVED -> {}
            SENT -> {}
        }
        navGraph?.let { navigation ->
            val intent = Intent(this, BedRockActivity::class.java)
            intent.putExtra(ROOT_ACTIVITY_NAV_GRAPH, navigation)
            startActivity(intent)
        }
    }

    private fun setNavigationDrawer() {
        binding.apply {
            navigationView.setNavigationItemSelectedListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.nav_profile -> viewModel.checkSession(PROFILE)
                    R.id.nav_saved -> viewModel.checkSession(SAVED)
                    R.id.nav_sent -> viewModel.checkSession(SENT)

                    R.id.nav_send_questions -> {}
                    R.id.nav_more_apps -> {
                        startActivity(
                            Intent(ACTION_VIEW, Uri.parse(getString(R.string.sq_mmfsin_url)))
                        )
                    }
                }
                drawerLayout.closeDrawers()
                true
            }
        }
    }

    private fun setListeners() {
        binding.apply {
            ivBack.setOnClickListener { openDrawer() }
        }
    }

    private fun openDrawer() = binding.drawerLayout.openDrawer(binding.navigationView)

    private fun setAds() {
        binding.apply {
            adView.visibility = View.GONE
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
}