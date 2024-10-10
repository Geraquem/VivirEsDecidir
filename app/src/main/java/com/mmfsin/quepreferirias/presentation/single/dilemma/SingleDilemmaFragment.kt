package com.mmfsin.quepreferirias.presentation.single.dilemma

import android.animation.ObjectAnimator
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.content.ContextCompat.getColor
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView.OnScrollChangeListener
import androidx.fragment.app.viewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.mmfsin.quepreferirias.R
import com.mmfsin.quepreferirias.base.BaseFragment
import com.mmfsin.quepreferirias.databinding.FragmentDilemmaBinding
import com.mmfsin.quepreferirias.domain.models.Comment
import com.mmfsin.quepreferirias.domain.models.Dilemma
import com.mmfsin.quepreferirias.domain.models.DilemmaVotes
import com.mmfsin.quepreferirias.domain.models.Session
import com.mmfsin.quepreferirias.presentation.dashboard.common.dialog.MenuDashboardBSheet
import com.mmfsin.quepreferirias.presentation.dashboard.common.interfaces.IMenuDashboardListener
import com.mmfsin.quepreferirias.presentation.dashboard.comments.CommentsFragment
import com.mmfsin.quepreferirias.presentation.dashboard.comments.dialogs.send.SendCommentBSheet
import com.mmfsin.quepreferirias.presentation.dashboard.dilemmas.interfaces.ICommentsListener
import com.mmfsin.quepreferirias.presentation.dashboard.dilemmas.interfaces.ISendCommentListener
import com.mmfsin.quepreferirias.presentation.main.BedRockActivity
import com.mmfsin.quepreferirias.presentation.models.DashboardType.DILEMMA
import com.mmfsin.quepreferirias.presentation.models.FavButtonTag.FAV
import com.mmfsin.quepreferirias.presentation.models.FavButtonTag.NO_FAV
import com.mmfsin.quepreferirias.presentation.models.Percents
import com.mmfsin.quepreferirias.presentation.single.dialogs.ErrorDataDialog
import com.mmfsin.quepreferirias.utils.DILEMMA_ID
import com.mmfsin.quepreferirias.utils.LOGIN_BROADCAST
import com.mmfsin.quepreferirias.utils.USER_ID
import com.mmfsin.quepreferirias.utils.checkNotNulls
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SingleDilemmaFragment : BaseFragment<FragmentDilemmaBinding, SingleDilemmaViewModel>(),
    IMenuDashboardListener, ISendCommentListener, ICommentsListener {

    override val viewModel: SingleDilemmaViewModel by viewModels()
    private lateinit var mContext: Context

    private var hasSession = false

    private var dilemmaId: String? = null
    private var actualData: Dilemma? = null

    private var isFav: Boolean? = null

    private lateinit var commentsFragment: CommentsFragment

    private var votesYes: Long = 0
    private var votesNo: Long = 0

    override fun inflateView(
        inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentDilemmaBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.checkSessionInitiated()
    }

    override fun getBundleArgs() {
        arguments?.let { bundle -> dilemmaId = bundle.getString(DILEMMA_ID) }
    }

    override fun setUI() {
        binding.apply {
            loadingFull.root.isVisible = true
            clBottom.isVisible = false
            setToolbar()
            setInitialConfig()

            nsvContainer.setOnScrollChangeListener(OnScrollChangeListener { v, _, scrollY, _, oldScrollY ->
                if (v.getChildAt(v.childCount - 1) != null) {
                    if (
                        (scrollY >= (v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight)) &&
                        scrollY > oldScrollY
                    ) {
                        if (::commentsFragment.isInitialized) commentsFragment.updateComments()
                    }
                }
            })
        }
    }

    private fun setToolbar() {
        (activity as BedRockActivity).apply {
            backListener { onBackPressed() }
            setToolbarText("")
        }
    }

    override fun setListeners() {
        binding.apply {
            btnYes.setOnClickListener { yesOrNoClick(isYes = true) }
            btnNo.setOnClickListener { yesOrNoClick(isYes = false) }

            tvCreatorName.setOnClickListener {
                checkNotNulls(actualData, actualData?.creatorId) { _, creatorId ->
                    viewModel.checkIfIsMe(creatorId)
                }
            }

            btnComment.setOnClickListener { sendComment() }
            btnFav.setOnClickListener { favOnClick() }
            btnShare.setOnClickListener { share() }
            btnMenu.setOnClickListener { openMenu() }
        }
    }

    private fun yesOrNoClick(isYes: Boolean) {
        actualData?.let { data ->
            viewModel.voteDilemma(data.id, isYes)
            hideButtons(isYes)
        } ?: run { error() }
    }

    private fun hideButtons(isYes: Boolean) {
        binding.apply {
            if (isYes) {
                btnYes.setImageResource(R.drawable.ic_dilemma_yes)
                percents.ivYes.visibility = View.VISIBLE
            } else {
                btnNo.setImageResource(R.drawable.ic_dilemma_no)
                percents.ivNo.visibility = View.VISIBLE
            }
            btnYes.isEnabled = false
            btnNo.isEnabled = false
            llButtons.animate().alpha(0.0f).duration = 250
        }
    }

    private fun openMenu() {
        val dialog = MenuDashboardBSheet(isFav, this@SingleDilemmaFragment)
        activity?.let { dialog.show(it.supportFragmentManager, "") }
    }

    override fun sendComment() = viewModel.getSessionToComment()

    override fun setFavorite() = favOnClick()

    private fun openSendCommentSheet(session: Session?) {
        session?.let { userData ->
            actualData?.let { dilemma ->
                val dialog =
                    SendCommentBSheet(dilemma, DILEMMA, userData, this@SingleDilemmaFragment)
                activity?.let { dialog.show(it.supportFragmentManager, "") }
            }
        } ?: run { localBroadcastOpenLogin() }
    }

    override fun commentSent(comment: Comment) {
        if (::commentsFragment.isInitialized) commentsFragment.commentSent(comment)
    }

    override fun copyText() {
        checkNotNulls(activity, actualData) { a, data ->
            val clipboard = a.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val text = "${data.txtTop} ${getString(R.string.dashboard_but)} ${data.txtBottom}"
            val clip = ClipData.newPlainText("label", text)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(
                mContext,
                getString(R.string.menu_dashboard_dilemma_copied),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun share() {
        actualData?.let { data ->
            val text = "${data.txtTop} ${getString(R.string.dashboard_but)} ${data.txtBottom}, " +
                    "${getString(R.string.dashboard_share_question)} \n\n" +
                    "${getString(R.string.dashboard_share_more_in)}\n" +
                    getString(R.string.dashboard_share_url)
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, text)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }
    }

    override fun report() {
        actualData?.let { data -> viewModel.reportDilemma(data) }
    }

    override fun observe() {
        viewModel.event.observe(this) { event ->
            when (event) {
                is SingleDilemmaEvent.InitiatedSession -> {
                    hasSession = event.initiatedSession
                    dilemmaId?.let { id -> viewModel.getSingleDilemma(id) } ?: run { error() }
                }

                is SingleDilemmaEvent.ReCheckSession -> hasSession = event.initiatedSession

                is SingleDilemmaEvent.SingleDilemma -> {
                    actualData = event.data
                    setData()
                }

                is SingleDilemmaEvent.CheckDilemmaFav -> {
                    if (event.result) setFavButton(isOn = true)
                    else setFavButton(isOn = false)
                    isFav = event.result
                    setUpComments()
                    actualData?.let { d -> viewModel.getVotes(d.id) }
                }

                is SingleDilemmaEvent.GetPercents -> setPercents(event.percents)

                is SingleDilemmaEvent.GetVotes -> {
                    setUpVotes(event.votes)
                    actualData?.let { viewModel.checkIfVoted(it.id) }
                }

                is SingleDilemmaEvent.VoteDilemma -> {
                    if (event.wasYes) votesYes++ else votesNo++
                    viewModel.getPercents(votesYes, votesNo)
                }

                is SingleDilemmaEvent.GetSessionToComment -> openSendCommentSheet(event.session)
                is SingleDilemmaEvent.AlreadyVoted -> checkAlreadyVoted(event.voted)
                is SingleDilemmaEvent.NavigateToProfile -> toUserProfile(event.isMe, event.userId)
                is SingleDilemmaEvent.Reported -> reported()
                is SingleDilemmaEvent.SWW -> error()
            }
        }
    }

    private fun setData() {
        try {
            binding.apply {
                setInitialConfig()
                actualData?.let { data ->
                    viewModel.checkIfIsFav(data.id)
                    tvTextTop.text = data.txtTop
                    tvTextBottom.text = data.txtBottom
                    data.creatorName?.let { name ->
                        tvCreatorName.text = name
                        llCreatorName.visibility = View.VISIBLE
                    } ?: run { llCreatorName.visibility = View.GONE }
                }
            }
        } catch (e: Exception) {
            error()
        }
    }

    private fun setInitialConfig() {
        binding.apply {
            btnYes.setImageResource(R.drawable.ic_dilemma_yes_trans)
            btnNo.setImageResource(R.drawable.ic_dilemma_no_trans)
            percents.apply {
                root.animate().alpha(0.0f).duration = 250
                animateProgress(progressBarLeft, 0, 0)
                animateProgress(progressBarRight, 0, 0)
                ivYes.visibility = View.INVISIBLE
                ivNo.visibility = View.INVISIBLE
                btnYes.isEnabled = true
                btnNo.isEnabled = true
            }
        }
    }

    private fun setPercents(actualPercents: Percents) {
        binding.apply {
            percents.apply {
                tvPercentYes.text = actualPercents.percentYesTop
                tvPercentNo.text = actualPercents.percentNoBottom
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

    private fun setUpComments() {
        actualData?.let { d ->
            commentsFragment = CommentsFragment(d.id, DILEMMA, this@SingleDilemmaFragment)
            childFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, commentsFragment)
                .commit()
        }
    }

    private fun setUpVotes(dilemmaVotes: DilemmaVotes) {
        binding.apply {
            votesYes = dilemmaVotes.votesYes
            votesNo = dilemmaVotes.votesNo
        }
    }

    private fun checkAlreadyVoted(voted: Boolean?) {
        /** if null -> no voted */
        binding.apply {
            voted?.let {
                hideButtons(it)
                viewModel.getPercents(votesYes, votesNo)
            }
            loadingFull.root.isVisible = false
        }
    }

    private fun favOnClick() {
        if (hasSession) {
            binding.btnFav.apply {
                actualData?.let { data ->
                    when (tag) {
                        FAV -> {
                            setFavButton(isOn = false)
                            viewModel.deleteDilemmaFav(data.id)
                        }

                        NO_FAV -> {
                            setFavButton(isOn = true)
                            viewModel.setDilemmaFav(data.id, data.txtTop, data.txtBottom)
                        }

                        else -> Log.e("FavButton", "Unexpected click")
                    }
                } ?: run { error() }
            }
        } else localBroadcastOpenLogin()
    }

    private fun setFavButton(isOn: Boolean) {
        binding.apply {
            btnFav.apply {
                imageTintList = if (isOn) {
                    tag = FAV
                    isFav = true
                    animate().rotation(720f).setDuration(350).start()
                    setImageResource(R.drawable.ic_fav_on)
                    ColorStateList.valueOf(getColor(mContext, R.color.saved))
                } else {
                    tag = NO_FAV
                    isFav = false
                    animate().rotation(0f).setDuration(350).start()
                    setImageResource(R.drawable.ic_fav_off)
                    ColorStateList.valueOf(getColor(mContext, R.color.black))
                }
            }
        }
    }


    private fun localBroadcastOpenLogin() =
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(Intent(LOGIN_BROADCAST))

    override fun navigateToUserProfile(userId: String) = viewModel.checkIfIsMe(userId)

    private fun toUserProfile(isMe: Boolean, userId: String) {
        val navGraph = if (isMe) R.navigation.nav_graph_profile
        else R.navigation.nav_graph_other_profile
        (activity as BedRockActivity).openActivity(navGraph, USER_ID, userId)
    }

    private fun reported() {
        Toast.makeText(mContext, "reportado", Toast.LENGTH_SHORT).show()
    }

    override fun shouldInitiateSession() {
        localBroadcastOpenLogin()
    }

    private fun error() {
        val dialog = ErrorDataDialog { activity?.onBackPressedDispatcher?.onBackPressed() }
        activity?.let { dialog.show(it.supportFragmentManager, "") }
    }

    override fun onResume() {
        super.onResume()
        viewModel.reCheckSession()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
}

