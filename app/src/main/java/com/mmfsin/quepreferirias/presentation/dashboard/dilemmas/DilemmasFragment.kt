package com.mmfsin.quepreferirias.presentation.dashboard.dilemmas

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.mmfsin.quepreferirias.R
import com.mmfsin.quepreferirias.base.BaseFragment
import com.mmfsin.quepreferirias.databinding.FragmentDilemmaBinding
import com.mmfsin.quepreferirias.domain.models.Comment
import com.mmfsin.quepreferirias.domain.models.CommentVote
import com.mmfsin.quepreferirias.domain.models.Dilemma
import com.mmfsin.quepreferirias.domain.models.DilemmaVotes
import com.mmfsin.quepreferirias.presentation.dashboard.common.dialog.MenuDashboardDialog
import com.mmfsin.quepreferirias.presentation.dashboard.common.dialog.NoMoreDialog
import com.mmfsin.quepreferirias.presentation.dashboard.common.interfaces.IMenuDashboardListener
import com.mmfsin.quepreferirias.presentation.dashboard.dilemmas.adapter.CommentsAdapter
import com.mmfsin.quepreferirias.presentation.dashboard.dilemmas.comments.CommentsSheet
import com.mmfsin.quepreferirias.presentation.dashboard.dilemmas.listener.IBSheetListener
import com.mmfsin.quepreferirias.presentation.dashboard.dilemmas.listener.ICommentsListener
import com.mmfsin.quepreferirias.presentation.main.BedRockActivity
import com.mmfsin.quepreferirias.presentation.models.FavButtonTag.FAV
import com.mmfsin.quepreferirias.presentation.models.FavButtonTag.NO_FAV
import com.mmfsin.quepreferirias.presentation.models.Percents
import com.mmfsin.quepreferirias.utils.LOGIN_BROADCAST
import com.mmfsin.quepreferirias.utils.USER_ID
import com.mmfsin.quepreferirias.utils.checkNotNulls
import com.mmfsin.quepreferirias.utils.showErrorDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DilemmasFragment : BaseFragment<FragmentDilemmaBinding, DilemmasViewModel>(),
    IBSheetListener, ICommentsListener, IMenuDashboardListener {

    override val viewModel: DilemmasViewModel by viewModels()
    private lateinit var mContext: Context

    private var hasSession = false

    private var dilemmaList = emptyList<Dilemma>()
    private var actualData: Dilemma? = null
    private var position: Int = 0

    private var isFav: Boolean? = null

    private var commentsAdapter: CommentsAdapter? = null

    private var votesYes: Long = 0
    private var votesNo: Long = 0

    override fun inflateView(
        inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentDilemmaBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.checkSessionInitiated()
    }

    override fun setUI() {
        binding.apply {
            loadingFull.root.isVisible = true
            loadingComments.root.isVisible = true
            setToolbar()
            setInitialConfig()

            nsvContainer.setOnScrollChangeListener(OnScrollChangeListener { v, _, scrollY, _, oldScrollY ->
                if (v.getChildAt(v.childCount - 1) != null) {
                    if (
                        (scrollY >= (v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight)) &&
                        scrollY > oldScrollY
                    ) {
                        comments.loadingMore.isVisible = true
                        actualData?.let { d -> viewModel.getComments(d.id, initialLoad = false) }
                    }
                }
            })
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

            tvCreatorName.setOnClickListener {
                checkNotNulls(actualData, actualData?.creatorId) { _, creatorId ->
//                    viewModel.checkIfIsMe(creatorId)
                }
            }

            btnComments.setOnClickListener { openAllComments() }
            btnFav.setOnClickListener { favOnClick() }
            btnMenu.setOnClickListener { openMenu() }

            btnNext.setOnClickListener {
                position++
                if (position < dilemmaList.size) {
//                    showInterstitial()
                    loadingComments.root.isVisible = true
                    llButtons.animate().alpha(1f).duration = 250
                    percents.root.animate().alpha(0.0f).duration = 250
                    setInitialConfig()
                    setData()
                } else {
                    activity?.let {
                        val dialog = NoMoreDialog {
                            activity?.onBackPressedDispatcher?.onBackPressed()
                        }
                        dialog.show(it.supportFragmentManager, "")
                    }
                }
            }
        }
    }

    private fun yesOrNoClick(isYes: Boolean) {
        binding.apply {
            actualData = dilemmaList[position]
            actualData?.let { data ->
                viewModel.voteDilemma(data.id, isYes)
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
            } ?: run { error() }
        }
    }

    private fun openAllComments() {
        actualData?.let { dilemma ->
            val modalBottomSheet = CommentsSheet(dilemmaId = dilemma.id, this@DilemmasFragment)
            activity?.let { modalBottomSheet.show(it.supportFragmentManager, "") }
        } ?: run { error() }
    }

    private fun openMenu() {
        val dialog = MenuDashboardDialog(isFav, this@DilemmasFragment)
        activity?.let { dialog.show(it.supportFragmentManager, "") }
    }

    override fun openComments() = openAllComments()
    override fun setFavorite() = favOnClick()

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
                is DilemmasEvent.InitiatedSession -> {
                    hasSession = event.initiatedSession
                    viewModel.getDilemmas()
                }

                is DilemmasEvent.ReCheckSession -> hasSession = event.initiatedSession

                is DilemmasEvent.Dilemmas -> {
                    dilemmaList = event.data
                    setData()
                }

                is DilemmasEvent.CheckDilemmaFav -> {
                    if (event.result) setFavButton(isOn = true)
                    else setFavButton(isOn = false)
                    isFav = event.result
                    actualData?.let { d -> viewModel.getComments(d.id, initialLoad = true) }
                }

                is DilemmasEvent.GetComments -> {
                    setUpComments(event.comments)
                    actualData?.let { d -> viewModel.getVotes(d.id) }
                }

                is DilemmasEvent.GetVotes -> setUpVotes(event.votes)
                is DilemmasEvent.GetPercents -> setPercents(event.percents)

                is DilemmasEvent.VoteDilemma -> {
                    if (event.wasYes) votesYes++ else votesNo++
                    viewModel.getPercents(votesYes, votesNo)
                }

                is DilemmasEvent.CommentAlreadyVoted -> {
                    Toast.makeText(
                        activity?.applicationContext,
                        getString(R.string.comment_already_voted),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is DilemmasEvent.CommentVotedResult -> updateCommentVotes(
                    event.vote,
                    event.position,
                    event.alreadyVoted
                )

                is DilemmasEvent.NavigateToProfile -> toUserProfile(event.isMe, event.userId)
                is DilemmasEvent.Reported -> reported()
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
        commentsAdapter?.clearData()
        commentsAdapter = null
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

    private fun setUpComments(comments: List<Comment>) {
        binding.apply {
            binding.comments.apply {
                if (rvComments.adapter == null) {
                    tvNoComments.isVisible = comments.isEmpty()
                    rvComments.apply {
                        layoutManager = LinearLayoutManager(mContext)
                        commentsAdapter = CommentsAdapter(
                            comments as MutableList<Comment>,
                            this@DilemmasFragment
                        )
                        adapter = commentsAdapter
                    }
                } else commentsAdapter?.addComments(comments)
                loadingMore.isVisible = false
            }
            loadingComments.root.isVisible = false
        }
    }

    private fun setUpVotes(dilemmaVotes: DilemmaVotes) {
        binding.apply {
            votesYes = dilemmaVotes.votesYes
            votesNo = dilemmaVotes.votesNo
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

    override fun refreshComments() {
//         viewModel.getComments(initialLoad = true)
    }

    private fun localBroadcastOpenLogin() =
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(Intent(LOGIN_BROADCAST))

    override fun navigateToUserProfile(userId: String) = viewModel.checkIfIsMe(userId)

    private fun toUserProfile(isMe: Boolean, userId: String) {
        val navGraph = if (isMe) R.navigation.nav_graph_profile
        else R.navigation.nav_graph_other_profile
        (activity as BedRockActivity).openActivity(navGraph, USER_ID, userId)
    }

    override fun onCommentNameClick(userId: String) = navigateToUserProfile(userId)

    override fun respondComment() {}

    override fun voteComment(
        commentId: String,
        vote: CommentVote,
        likes: Long,
        position: Int
    ) {
        actualData?.let { data ->
            if (hasSession) viewModel.voteComment(data.id, commentId, vote, likes, position)
            else {
                Toast.makeText(activity?.applicationContext, "no session", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun updateCommentVotes(vote: CommentVote, position: Int, alreadyVoted: Boolean) {
        commentsAdapter?.updateCommentVotes(vote, position, alreadyVoted)
    }

    private fun reported() {
        Toast.makeText(mContext, "reportado", Toast.LENGTH_SHORT).show()
    }

    private fun error() {
        activity?.showErrorDialog { activity?.finish() }
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

