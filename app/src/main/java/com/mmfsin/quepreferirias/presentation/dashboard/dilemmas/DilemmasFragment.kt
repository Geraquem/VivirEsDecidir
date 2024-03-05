package com.mmfsin.quepreferirias.presentation.dashboard.dilemmas

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.mmfsin.quepreferirias.R
import com.mmfsin.quepreferirias.base.BaseFragment
import com.mmfsin.quepreferirias.databinding.FragmentDilemmaBinding
import com.mmfsin.quepreferirias.domain.models.Comment
import com.mmfsin.quepreferirias.domain.models.Dilemma
import com.mmfsin.quepreferirias.presentation.dashboard.dialog.NoMoreDialog
import com.mmfsin.quepreferirias.presentation.dashboard.dilemmas.adapter.CommentsAdapter
import com.mmfsin.quepreferirias.presentation.dashboard.dilemmas.comments.CommentsSheet
import com.mmfsin.quepreferirias.presentation.main.BedRockActivity
import com.mmfsin.quepreferirias.presentation.models.Percents
import com.mmfsin.quepreferirias.utils.LAST_COMMENTS
import com.mmfsin.quepreferirias.utils.showErrorDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DilemmasFragment : BaseFragment<FragmentDilemmaBinding, DilemmasViewModel>() {

    override val viewModel: DilemmasViewModel by viewModels()

    private var dilemmaList = emptyList<Dilemma>()
    private var actualData: Dilemma? = null
    private var position: Int = 0

    private var votesYes: Long = 0
    private var votesNo: Long = 0

    private var comments = emptyList<Comment>()

    private lateinit var mContext: Context

    override fun inflateView(
        inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentDilemmaBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getConditionalData()
    }

    override fun setUI() {
        binding.apply {
            loadingScreen.root.isVisible
            setToolbar()
            setInitialConfig()
        }
    }

    private fun setToolbar() {
        (activity as BedRockActivity).apply {
            backListener { onBackPressed() }
            setToolbarText(R.string.nav_dilemmas)
        }
    }

    override fun setListeners() {
        binding.apply {
            btnYes.setOnClickListener { yesOrNoClick(isYes = true) }
            btnNo.setOnClickListener { yesOrNoClick(isYes = false) }

            ivFav.setOnClickListener { setFavDilemma() }

            ivOpenComments.setOnClickListener { openAllComments() }
            comments.llSeeAll.setOnClickListener { openAllComments() }

            btnNext.btnNext.setOnClickListener {
                position++
                if (position < dilemmaList.size) {
//                    showInterstitial()
                    comments.loading.root.visibility = View.VISIBLE
                    llButtons.animate().alpha(1f).duration = 250
                    percents.root.animate().alpha(0.0f).duration = 250
                    setInitialConfig()
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

    private fun openAllComments() {
        actualData?.let { dilemma ->
            val modalBottomSheet = CommentsSheet(
                dilemmaId = dilemma.id,
                comments = this@DilemmasFragment.comments
            )
            activity?.let { modalBottomSheet.show(it.supportFragmentManager, "") }
        } ?: run { error() }
    }

    override fun observe() {
        viewModel.event.observe(this) { event ->
            when (event) {
                is DilemmasEvent.Data -> {
                    dilemmaList = event.data
                    setData()
                }

                is DilemmasEvent.GetPercents -> setPercents(event.percents)
                is DilemmasEvent.GetComments -> {
                    comments = event.comments
                    setUpComments(comments.take(LAST_COMMENTS))
                }

                is DilemmasEvent.SWW -> error()
            }
        }
    }

    private fun setData() {
        try {
            binding.apply {
                setInitialConfig()
                actualData = dilemmaList[position]
                actualData?.let { data ->
                    viewModel.getComments(data.id)
                    tvTextTop.text = data.topText
                    tvTextBottom.text = data.bottomText
                    data.creatorName?.let { name ->
                        tvCreatorName.text = name
                        llCreatorName.visibility = View.VISIBLE
                    } ?: run { llCreatorName.visibility = View.GONE }
                    votesYes = data.votesYes
                    votesNo = data.votesNo
                    loadingScreen.root.isVisible = false
                }
            }
        } catch (e: Exception) {
            error()
        }
    }

    private fun setInitialConfig() {
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
            ivFav.animate().rotation(0f).setDuration(0).start()
            ivFav.setImageResource(R.drawable.ic_fav_off)
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

    private fun setUpComments(lastComments: List<Comment>) {
        binding.comments.apply {
            val title = when (comments.size) {
                0 -> getString(R.string.dashboard_no_comments)
                1 -> getString(R.string.dashboard_single_comment_title)
                else -> getString(R.string.dashboard_comments_title, comments.size.toString())
            }
            tvTitle.text = title
            llSeeAll.visibility = if (comments.size < LAST_COMMENTS) View.GONE else View.VISIBLE
            rvComments.apply {
                layoutManager = LinearLayoutManager(mContext)
                adapter = CommentsAdapter(lastComments)
            }
            loading.root.visibility = View.GONE
        }
    }

    private fun setFavDilemma() {
        binding.apply {
            ivFav.animate().rotation(360f).setDuration(350).start()
            ivFav.setImageResource(R.drawable.ic_fav_on)
        }
    }

    private fun error() {
        activity?.showErrorDialog { activity?.finish() }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
}