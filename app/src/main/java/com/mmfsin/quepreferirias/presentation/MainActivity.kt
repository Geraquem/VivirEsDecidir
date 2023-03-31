package com.mmfsin.quepreferirias.presentation

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.mmfsin.quepreferirias.R
import com.mmfsin.quepreferirias.databinding.ActivityMainBinding
import com.mmfsin.quepreferirias.domain.models.DataDTO

class MainActivity : AppCompatActivity(), MainView {

    private lateinit var binding: ActivityMainBinding

    private lateinit var dataList: List<DataDTO>
    private var position = 0

    private val presenter by lazy { MainPresenter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUI()
        setListeners()
//        presenter.getData()
    }

    private fun setUI() {
        binding.apply {
//            showWelcomeDialog()
//            loadingScreen.root.visibility = View.VISIBLE
        }
    }

//    fun animatePercentage(percentage: Float) {
//        val width = percentage * binding.customView.width / 100
//        val animator = ValueAnimator.ofInt(0, width.toInt())
//        animator.addUpdateListener { valueAnimator ->
//            val value = valueAnimator.animatedValue as Int
//            binding.progressBar.layoutParams.width = value
//            binding.progressBar.requestLayout()
//        }
//        animator.duration = 1000
//        animator.start()
//    }

    override fun firebaseReady(dataList: List<DataDTO>) {
        this.dataList = dataList
        setSingleData(dataList[position])
    }

    override fun setSingleData(data: DataDTO) {
        binding.apply {
            tvTextTop.text = data.textA
            tvTextBottom.text = data.textB
            loadingScreen.root.visibility = View.GONE
        }
    }

    private fun setListeners() {
        binding.apply {
            btnYes.setOnClickListener { }

            btnNo.setOnClickListener {

                val votesYes = 35
                val votesNo = 65
                val max = votesYes + votesNo

                percents.progressBarLeft.max = max

                ObjectAnimator.ofInt(percents.progressBarLeft, "progress", votesYes)
                    .setDuration(2500).start()

                percents.progressBarRight.max = max

                ObjectAnimator.ofInt(percents.progressBarRight, "progress", votesNo)
                    .setDuration(2500).start()

            }
        }
    }
}