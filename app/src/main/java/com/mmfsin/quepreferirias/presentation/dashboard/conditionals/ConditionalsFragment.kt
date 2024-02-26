package com.mmfsin.quepreferirias.presentation.dashboard.conditionals

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
import com.mmfsin.quepreferirias.databinding.FragmentConditionalDataBinding
import com.mmfsin.quepreferirias.domain.models.ConditionalData
import com.mmfsin.quepreferirias.presentation.dashboard.dialog.NoMoreDialog
import com.mmfsin.quepreferirias.presentation.models.Percents
import com.mmfsin.quepreferirias.utils.showErrorDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConditionalsFragment : BaseFragment<FragmentConditionalDataBinding, ConditionalsViewModel>() {

    override val viewModel: ConditionalsViewModel by viewModels()

    private var conditionalDataList = emptyList<ConditionalData>()
    private var position: Int = 0

    private var votesYes: Long = 0
    private var votesNo: Long = 0

    private lateinit var mContext: Context

    override fun inflateView(
        inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentConditionalDataBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getConditionalData()
    }

    override fun setUI() {
        binding.apply {
            loadingScreen.root.isVisible
            setInitialPercents()
        }
    }

    override fun setListeners() {
        binding.apply {
            btnYes.setOnClickListener { yesOrNoClick(isYes = true) }
            btnNo.setOnClickListener { yesOrNoClick(isYes = false) }

            btnNext.setOnClickListener {
                position++
                if (position < conditionalDataList.size) {
//                    showInterstitial()
                    llButtons.animate().alpha(1f).duration = 250
                    percents.root.animate().alpha(0.0f).duration = 250
                    setInitialPercents()
                    setData()
                } else {
                    activity?.let {
                        val dialog = NoMoreDialog() { /** TODO */ }
                        dialog.show(it.supportFragmentManager, "")
                    }
                }
            }
        }
    }

    private fun yesOrNoClick(isYes: Boolean) {
        binding.apply {
            if (isYes) {
                btnYes.setImageResource(R.drawable.ic_option_yes)
                percents.ivYes.visibility = View.VISIBLE
            } else {
                btnNo.setImageResource(R.drawable.ic_option_no)
                percents.ivNo.visibility = View.VISIBLE
            }
            llButtons.animate().alpha(0.0f).duration = 250
            viewModel.getPercents(votesYes, votesNo)
        }
    }

    override fun observe() {
        viewModel.event.observe(this) { event ->
            when (event) {
                is ConditionalsEvent.Data -> {
                    conditionalDataList = event.data
                    setData()
                }

                is ConditionalsEvent.GetPercents -> setPercents(event.percents)
                is ConditionalsEvent.SWW -> error()
            }
        }
    }

    private fun setData() {
        try {
            binding.apply {
                setInitialPercents()
                val actualData = conditionalDataList[position]
                tvTextTop.text = actualData.topText
                tvTextBottom.text = actualData.bottomText
                actualData.creatorName?.let { name ->
                    tvCreatorName.text = name
                    llCreatorName.visibility = View.VISIBLE
                } ?: run { llCreatorName.visibility = View.GONE }
                votesYes = actualData.votesYes
                votesNo = actualData.votesNo
                loadingScreen.root.isVisible = false
            }
        } catch (e: Exception) {
            error()
        }
    }

    private fun setInitialPercents() {
        binding.apply {
            btnYes.setImageResource(R.drawable.ic_option_yes_trans)
            btnNo.setImageResource(R.drawable.ic_option_no_trans)
            percents.apply {
                root.animate().alpha(0.0f).duration = 250
                animateProgress(progressBarLeft, 0, 0)
                animateProgress(progressBarRight, 0, 0)
                ivYes.visibility = View.INVISIBLE
                ivNo.visibility = View.INVISIBLE
            }
        }
    }

    private fun setPercents(actualPercents: Percents) {
        binding.apply {
            percents.apply {
                tvPercentYes.text = actualPercents.percentYes
                tvPercentNo.text = actualPercents.percentNo
                tvVotesYes.text = votesYes.toString()
                tvVotesNo.text = votesNo.toString()
                val total = (votesYes + votesNo).toInt()
                percents.root.animate().alpha(1f).duration = 250
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