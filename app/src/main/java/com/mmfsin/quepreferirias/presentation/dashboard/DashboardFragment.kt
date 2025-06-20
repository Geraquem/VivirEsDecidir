package com.mmfsin.quepreferirias.presentation.dashboard

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.mmfsin.quepreferirias.R
import com.mmfsin.quepreferirias.base.BaseFragment
import com.mmfsin.quepreferirias.databinding.FragmentDashboardBinding
import com.mmfsin.quepreferirias.domain.models.Data
import com.mmfsin.quepreferirias.presentation.MainActivity
import com.mmfsin.quepreferirias.presentation.dashboard.dialog.NoMoreDialog
import com.mmfsin.quepreferirias.presentation.models.Percents
import com.mmfsin.quepreferirias.utils.showErrorDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment : BaseFragment<FragmentDashboardBinding, DashboardViewModel>() {

    override val viewModel: DashboardViewModel by viewModels()

    private lateinit var mContext: Context

    private var dataList = emptyList<Data>()
    private var actualData: Data? = null
    private var position: Int = 0

    private var votesYes: Long = 0
    private var votesNo: Long = 0

    override fun inflateView(
        inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentDashboardBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAppData()
    }

    override fun setUI() {
        binding.apply {
            loadingScreen.root.isVisible
            position = 0
            votesYes = 0
            votesNo = 0
            percents.root.visibility = View.INVISIBLE
        }
        (activity as MainActivity).showBanner()
    }

    override fun setListeners() {
        binding.apply {
            btnSendQuestions.setOnClickListener {
                (activity as MainActivity).showSendQuestions()
            }

            btnYes.setOnClickListener { yesOrNoClick(isYes = true) }
            btnNo.setOnClickListener { yesOrNoClick(isYes = false) }

            btnNext.setOnClickListener {
                position++
                if (position < dataList.size) {
                    showInterstitial()
                    actualData = dataList[position]
                    binding.loadingScreen.root.isVisible
                    setData()
                } else {
                    activity?.let {
                        val dialog = NoMoreDialog() { it.recreate() }
                        dialog.show(it.supportFragmentManager, "")
                    }
                }
            }
        }
    }

    private fun showInterstitial() {
        if (position % 20 == 0) (activity as MainActivity).showInterstitial()
    }

    private fun yesOrNoClick(isYes: Boolean) {
        binding.apply {
            actualData?.let { data ->
                viewModel.vote(data.id, isYes)
                votesYes = data.votesYes
                votesNo = data.votesNo
                if (isYes) votesYes += 1 else votesNo += 1
                viewModel.getPercents(votesYes, votesNo)
                if (isYes) btnYes.setImageResource(R.drawable.ic_option_yes)
                else btnNo.setImageResource(R.drawable.ic_option_no)
                btnYes.isEnabled = false
                btnNo.isEnabled = false
            }
        }
    }

    override fun observe() {
        viewModel.event.observe(this) { event ->
            when (event) {
                is DashboardEvent.AppData -> {
                    dataList = event.data.shuffled()
                    actualData = dataList[position]
                    setData()
                }

                is DashboardEvent.GetPercents -> setPercents(event.percents)
                is DashboardEvent.SWW -> error()
            }
        }
    }

    private fun setData() {
        binding.apply {
            votesYes = 0
            votesNo = 0
            percents.root.visibility = View.INVISIBLE
            tvTextTop.text = actualData?.topText
            tvTextBottom.text = actualData?.bottomText
            actualData?.creatorName?.let { name ->
                tvCreatorName.text = name
                llCreatorName.visibility = View.VISIBLE
            } ?: run { llCreatorName.visibility = View.GONE }
            animateProgress(percents.progressBarLeft, 0, 0)
            animateProgress(percents.progressBarRight, 0, 0)
            btnYes.setImageResource(R.drawable.ic_option_yes_trans)
            btnNo.setImageResource(R.drawable.ic_option_no_trans)
            btnYes.isEnabled = true
            btnNo.isEnabled = true
            loadingScreen.root.isVisible = false
        }
    }

    private fun setPercents(actualPercents: Percents) {
        binding.apply {
            percents.apply {
                tvPercentYes.text = actualPercents.percentYes
                tvPercentNo.text = actualPercents.percentNo
                tvVotesYes.text = votesYes.toString()
                tvVotesNo.text = votesNo.toString()
                root.visibility = View.VISIBLE
                val total = (votesYes + votesNo).toInt()
                animateProgress(progressBarLeft, total, votesYes.toInt())
                animateProgress(progressBarRight, total, votesNo.toInt())
            }
        }
    }

    private fun animateProgress(progress: ProgressBar, total: Int, votes: Int) {
        progress.max = total * 100
        val animation = ObjectAnimator.ofInt(progress, "progress", votes * 100)
        animation.duration = 2000
        animation.interpolator = DecelerateInterpolator()
        animation.start()
    }

    private fun error() {
        activity?.showErrorDialog() { activity?.finish() }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
}