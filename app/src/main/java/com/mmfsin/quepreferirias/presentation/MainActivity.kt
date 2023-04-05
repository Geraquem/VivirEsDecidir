package com.mmfsin.quepreferirias.presentation

import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.mmfsin.quepreferirias.NO
import com.mmfsin.quepreferirias.R
import com.mmfsin.quepreferirias.YES
import com.mmfsin.quepreferirias.databinding.ActivityMainBinding
import com.mmfsin.quepreferirias.domain.models.DataDTO

class MainActivity : AppCompatActivity(), MainView {

    private lateinit var binding: ActivityMainBinding

    private lateinit var dataList: List<DataDTO>
    private var position = 0

    private var votesA: Long = 0
    private var votesB: Long = 0

    private var dataKey: String = ""

    private var mInterstitialAd: InterstitialAd? = null
    private var mInterstitialId = "ca-app-pub-3940256099942544/1033173712"
//    private var InterstitialId = "ca-app-pub-4515698012373396/6775142518"

    private val presenter by lazy { MainPresenter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        MobileAds.initialize(this) {}
        loadInterstitial(AdRequest.Builder().build())

        setUI()
        setListeners()
        presenter.getData()
    }

    private fun setUI() {
        binding.apply {
            loadingScreen.root.visibility = View.VISIBLE

            val adRequest = AdRequest.Builder().build()
            adView.loadAd(adRequest)

            val animationDrawable = clMain.background as AnimationDrawable
            animationDrawable.setEnterFadeDuration(6000)
            animationDrawable.setExitFadeDuration(6000)
            animationDrawable.start()

            percents.root.visibility = View.INVISIBLE
        }
    }

    override fun firebaseReady(dataList: List<DataDTO>) {
        this.dataList = dataList.shuffled()
        setSingleData(dataList[position])
    }

    override fun setSingleData(data: DataDTO) {
        binding.apply {
            dataKey = data.id
            tvTextTop.text = data.textA
            tvTextBottom.text = data.textB
            votesA = data.votesA
            votesB = data.votesB
            loadingScreen.root.visibility = View.GONE
        }
    }

    private fun setListeners() {
        binding.apply {
            ivMm.setOnClickListener {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.mmfsinURL))))
            }

            btnYes.setOnClickListener {
                presenter.setVotes(dataKey, YES)
                btnYes.isEnabled = false
                btnNo.isEnabled = false
                btnYes.setImageResource(R.drawable.ic_option_yes)
                votesA += 1
                showPercents()
            }

            btnNo.setOnClickListener {
                presenter.setVotes(dataKey, NO)
                btnYes.isEnabled = false
                btnNo.isEnabled = false
                btnNo.setImageResource(R.drawable.ic_option_no)
                votesA += 1
                showPercents()
            }

            btnNext.setOnClickListener {
                percents.root.visibility = View.INVISIBLE
                btnYes.setImageResource(R.drawable.ic_option_yes_trans)
                btnNo.setImageResource(R.drawable.ic_option_no_trans)
                btnYes.isEnabled = true
                btnNo.isEnabled = true

                animateProgress(percents.progressBarLeft, 0, 0)
                animateProgress(percents.progressBarRight, 0, 0)

                position += 1
                if (position < dataList.size) {
                    setSingleData(dataList[position])
                } else {
                    btnNext.text = getString(R.string.no_more_questions)
                    btnNext.isEnabled = false
                }
//                showInterstitial()
            }
        }
    }

    private fun showPercents() {
        binding.percents.apply {
            val progressA = progressBarLeft
            val progressB = progressBarRight
            val total = (votesA + votesB).toInt()

            val percents = presenter.calculatePercent(votesA, votesB)
            tvPercentYes.text = percents.first
            tvPercentNo.text = percents.second
            tvVotesYes.text = votesA.toString()
            tvVotesNo.text = votesB.toString()

            animateProgress(progressA, total, votesA.toInt())
            animateProgress(progressB, total, votesB.toInt())
            root.visibility = View.VISIBLE
        }
    }

    private fun animateProgress(progress: ProgressBar, total: Int, votes: Int) {
        progress.max = total * 100
        val animation = ObjectAnimator.ofInt(progress, "progress", votes * 100)
        animation.duration = 2000
        animation.interpolator = DecelerateInterpolator()
        animation.start()
    }

    private fun loadInterstitial(adRequest: AdRequest) {
        InterstitialAd.load(this,
            mInterstitialId,
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

    private fun showInterstitial() {
        if (position % 20 == 0) {
            mInterstitialAd?.let { ad ->
                ad.show(this)
                loadInterstitial(AdRequest.Builder().build())
            }
        }
    }
}