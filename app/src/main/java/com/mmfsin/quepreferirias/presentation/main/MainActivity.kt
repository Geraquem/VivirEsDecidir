package com.mmfsin.quepreferirias.presentation.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.bumptech.glide.Glide
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.mmfsin.quepreferirias.R
import com.mmfsin.quepreferirias.databinding.ActivityMainBinding
import com.mmfsin.quepreferirias.presentation.login.LoginActivity
import com.mmfsin.quepreferirias.presentation.models.DrawerFlow
import com.mmfsin.quepreferirias.presentation.models.DrawerFlow.DATA_SAVED
import com.mmfsin.quepreferirias.presentation.models.DrawerFlow.DILEMMAS
import com.mmfsin.quepreferirias.presentation.models.DrawerFlow.DUALISMS
import com.mmfsin.quepreferirias.presentation.models.DrawerFlow.MY_DATA
import com.mmfsin.quepreferirias.presentation.models.DrawerFlow.SEND_DATA
import com.mmfsin.quepreferirias.presentation.models.DrawerFlow.USER_PROFILE
import com.mmfsin.quepreferirias.presentation.send.dialogs.SendDataDialog
import com.mmfsin.quepreferirias.utils.LOGIN_BROADCAST
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
        registerLocalBroadcast()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getSession()
    }

    private fun init() {
        observe()
        setListeners()
        setNavigationDrawer()
        setAds()

        /*****************************/
        navigateDrawer(DILEMMAS)
//        openDrawer()
    }

    private fun observe() {
        viewModel.event.observe(this) { event ->
            when (event) {
                is MainEvent.DrawerFlowDirection -> {
                    val hasSession = event.result.first
                    if (hasSession) navigateDrawer(event.result.second) else loginFlow()
                }

                is MainEvent.GetSession -> {
                    event.session?.let { user ->
                        customNavigationDrawer(user.imageUrl, user.name)
                    } ?: run { drawerHeaderViewDefault() }
                }

                is MainEvent.SWW -> showErrorDialog() { finish() }
            }
        }
    }

    private fun loginFlow() =
        resultLauncher.launch(Intent(this@MainActivity, LoginActivity::class.java))

    private var resultLauncher = registerForActivityResult(StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            Toast.makeText(applicationContext, "BIENVENIDO/A", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateDrawer(flow: DrawerFlow) {
        when (flow) {
            DILEMMAS -> navigateTo(R.navigation.nav_graph_dilemmas)
            DUALISMS -> {}
            USER_PROFILE -> navigateTo(R.navigation.nav_graph_profile)
            DATA_SAVED -> navigateTo(R.navigation.nav_graph_saved_data)
            MY_DATA -> navigateTo(R.navigation.nav_graph_my_data)
            SEND_DATA -> openSendDataDialog()
        }
    }

    private fun navigateTo(navigation: Int) {
        val intent = Intent(this, BedRockActivity::class.java)
        intent.putExtra(ROOT_ACTIVITY_NAV_GRAPH, navigation)
        startActivity(intent)
    }

    private fun setNavigationDrawer() {
        binding.apply {
            navigationView.setNavigationItemSelectedListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.nav_dilemmas -> navigateDrawer(DILEMMAS)
                    R.id.nav_dualism -> {}
                    //////////////////////
                    R.id.nav_profile -> viewModel.checkSession(USER_PROFILE)
                    R.id.nav_saved -> viewModel.checkSession(DATA_SAVED)
                    R.id.nav_my_data -> viewModel.checkSession(MY_DATA)
                    //////////////////////
                    R.id.nav_send_data -> viewModel.checkSession(SEND_DATA)
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

    private fun drawerHeaderViewDefault() {
        binding.apply {
            val headerView = navigationView.getHeaderView(0)
            headerView.findViewById<ImageView>(R.id.iv_image)
                .setImageResource(R.drawable.ic_dilemma_yes)
            headerView.findViewById<TextView>(R.id.tv_title).text = getString(R.string.app_name)
        }
    }

    private fun customNavigationDrawer(image: String, name: String) {
        binding.apply {
            val headerView = navigationView.getHeaderView(0)
            val headerImage = headerView.findViewById<ImageView>(R.id.iv_image)
            Glide.with(this@MainActivity).load(image).into(headerImage)
            headerView.findViewById<TextView>(R.id.tv_title).text = name
        }
    }

    private fun setListeners() {
        binding.apply { ivMenu.setOnClickListener { openDrawer() } }
    }

    private fun openDrawer() = binding.drawerLayout.openDrawer(binding.navigationView)

    private fun openSendDataDialog() {
        SendDataDialog { navGraph -> navigateTo(navGraph) }.show(supportFragmentManager, "")
    }

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

    private fun registerLocalBroadcast() = LocalBroadcastManager.getInstance(this)
        .registerReceiver(mReceiver, IntentFilter(LOGIN_BROADCAST))

    private val mReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == LOGIN_BROADCAST) loginFlow()
        }
    }

    fun showInterstitial() {
        mInterstitialAd?.let { ad ->
            ad.show(this)
            loadInterstitial(AdRequest.Builder().build())
        }
    }

    private fun loadInterstitial(adRequest: AdRequest) {
//        InterstitialAd.load(this,
//            getString(R.string.insterstitial),
//            adRequest,
//            object : InterstitialAdLoadCallback() {
//                override fun onAdFailedToLoad(adError: LoadAdError) {
//                    mInterstitialAd = null
//                    loadInterstitial(AdRequest.Builder().build())
//                }
//
//                override fun onAdLoaded(interstitialAd: InterstitialAd) {
//                    mInterstitialAd = interstitialAd
//                }
//            })
    }
}