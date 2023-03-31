package com.mmfsin.quepreferirias.presentation

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.mmfsin.quepreferirias.R
import com.mmfsin.quepreferirias.databinding.ActivityMainBinding
import com.mmfsin.quepreferirias.domain.models.DataDTO

class MainActivity : AppCompatActivity(), MainView {

    private lateinit var binding: ActivityMainBinding

    private lateinit var dataList: List<DataDTO>
    private var position = 0

    private var votesA: Long = 0
    private var votesB: Long = 0

    private val presenter by lazy { MainPresenter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUI()
        setListeners()
        presenter.getData()
    }

    private fun setUI() {
        binding.apply {
//            showWelcomeDialog()
            loadingScreen.root.visibility = View.VISIBLE
            percents.root.visibility = View.INVISIBLE
        }
    }

    override fun firebaseReady(dataList: List<DataDTO>) {
        this.dataList = dataList
        setSingleData(dataList[position])
    }

    override fun setSingleData(data: DataDTO) {
        binding.apply {
            tvTextTop.text = data.textA
            tvTextBottom.text = data.textB
            votesA = data.votesA
            votesB = data.votesB
            loadingScreen.root.visibility = View.GONE
        }
    }

    private fun setListeners() {
        binding.apply {
            btnYes.setOnClickListener {
                btnYes.isEnabled = false
                btnNo.isEnabled = false
                //do firebase call
                btnYes.setImageResource(R.drawable.ic_option_yes)
                votesA += 1
                showPercents()
            }

            btnNo.setOnClickListener {
                btnYes.isEnabled = false
                btnNo.isEnabled = false
                //do firebase call
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
                setSingleData(dataList[position])
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
        progress.max = total
        ObjectAnimator.ofInt(progress, "progress", votes).setDuration(2500).start()
    }
}