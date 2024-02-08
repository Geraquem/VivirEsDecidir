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
import com.mmfsin.quepreferirias.databinding.FragmentDashboardBinding
import com.mmfsin.quepreferirias.domain.models.ConditionalData
import com.mmfsin.quepreferirias.presentation.dashboard.dialog.NoMoreDialog
import com.mmfsin.quepreferirias.presentation.main.MainActivity
import com.mmfsin.quepreferirias.presentation.models.Percents
import com.mmfsin.quepreferirias.utils.showErrorDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConditionalsFragment : BaseFragment<FragmentDashboardBinding, ConditionalsViewModel>() {

    override val viewModel: ConditionalsViewModel by viewModels()

    private lateinit var mContext: Context

    private var conditionalDataList = emptyList<ConditionalData>()
    private var actualConditionalData: ConditionalData? = null
    private var position: Int = 0

    private var votesYes: Long = 0
    private var votesNo: Long = 0

    override fun inflateView(
        inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentDashboardBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getConditionalData()
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
            btnYes.setOnClickListener { yesOrNoClick(isYes = true) }
            btnNo.setOnClickListener { yesOrNoClick(isYes = false) }

            btnNext.setOnClickListener {
                position++
                if (position < conditionalDataList.size) {
                    showInterstitial()
                    actualConditionalData = conditionalDataList[position]
                    binding.loadingScreen.root.isVisible
                    setData()
                } else {
                    activity?.let {
                        val dialog = NoMoreDialog() { /** TODO */ }
                        dialog.show(it.supportFragmentManager, "")
                        /** DELETE AFTER */
                        position = -1
                    }
                }
            }

            ivSave.setOnClickListener { actualConditionalData?.let { viewModel.saveDataToUser(it.id) }}
        }
    }

    private fun showInterstitial() {
        if (position % 20 == 0) (activity as MainActivity).showInterstitial()
    }

    private fun yesOrNoClick(isYes: Boolean) {
        binding.apply {
            actualConditionalData?.let { data ->
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
                is ConditionalsEvent.Data -> {
                    conditionalDataList = event.data//.shuffled()
                    actualConditionalData = conditionalDataList[position]
                    setData()
                }

                is ConditionalsEvent.GetPercents -> setPercents(event.percents)

                is ConditionalsEvent.AlreadySaved -> {
                    event.saved?.let { saveDataSRC(it) } ?: run { saveDataSRC(false) }
                }

                is ConditionalsEvent.DataSaved -> {
                    event.result?.let { saved ->
                        if (saved) saveDataSRC(true) else activity?.showErrorDialog() {}
                    } ?: run { (activity as MainActivity).loginFlow() }
                }

                is ConditionalsEvent.SWW -> error()
            }
        }
    }

    private fun saveDataSRC(isSaved: Boolean) {
        val resource = if (isSaved) R.drawable.ic_saved else R.drawable.ic_not_saved
        binding.ivSave.setImageResource(resource)
    }

    private fun setData() {
        actualConditionalData?.let {
            viewModel.checkIfAlreadySaved(conditionalDataList[position].id)
        }

        binding.apply {
            votesYes = 0
            votesNo = 0
            percents.root.visibility = View.INVISIBLE
            tvTextTop.text = actualConditionalData?.topText
            tvTextBottom.text = actualConditionalData?.bottomText
            actualConditionalData?.creatorName?.let { name ->
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