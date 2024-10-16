package com.mmfsin.quepreferirias.presentation.dashboard.dualisms

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList.valueOf
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getColor
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView.OnScrollChangeListener
import androidx.fragment.app.viewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.mmfsin.quepreferirias.R
import com.mmfsin.quepreferirias.base.BaseFragment
import com.mmfsin.quepreferirias.databinding.FragmentDualismBinding
import com.mmfsin.quepreferirias.domain.models.Comment
import com.mmfsin.quepreferirias.domain.models.Dualism
import com.mmfsin.quepreferirias.domain.models.DualismVotes
import com.mmfsin.quepreferirias.domain.models.Session
import com.mmfsin.quepreferirias.presentation.dashboard.comments.CommentsFragment
import com.mmfsin.quepreferirias.presentation.dashboard.comments.dialogs.send.SendCommentBSheet
import com.mmfsin.quepreferirias.presentation.dashboard.common.dialog.MenuDashboardBSheet
import com.mmfsin.quepreferirias.presentation.dashboard.common.dialog.NoMoreDialog
import com.mmfsin.quepreferirias.presentation.dashboard.common.interfaces.IMenuDashboardListener
import com.mmfsin.quepreferirias.presentation.dashboard.dilemmas.interfaces.ICommentsListener
import com.mmfsin.quepreferirias.presentation.dashboard.dilemmas.interfaces.ISendCommentListener
import com.mmfsin.quepreferirias.presentation.main.BedRockActivity
import com.mmfsin.quepreferirias.presentation.models.DashboardType.DUALISM
import com.mmfsin.quepreferirias.presentation.models.FavButtonTag
import com.mmfsin.quepreferirias.presentation.models.Percents
import com.mmfsin.quepreferirias.utils.LOGIN_BROADCAST
import com.mmfsin.quepreferirias.utils.USER_ID
import com.mmfsin.quepreferirias.utils.checkNotNulls
import com.mmfsin.quepreferirias.utils.countDown
import com.mmfsin.quepreferirias.utils.shareContent
import com.mmfsin.quepreferirias.utils.showErrorDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DualismsFragment : BaseFragment<FragmentDualismBinding, DualismsViewModel>(),
    ICommentsListener, IMenuDashboardListener, ISendCommentListener {

    override val viewModel: DualismsViewModel by viewModels()
    private lateinit var mContext: Context

    private var hasSession = false

    private var dualismList = emptyList<Dualism>()
    private var actualData: Dualism? = null
    private var position: Int = 0

    private var isFav: Boolean? = null

    private lateinit var commentsFragment: CommentsFragment

    private var votesTop: Long = 0
    private var votesBottom: Long = 0

    override fun inflateView(
        inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentDualismBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.checkSessionInitiated()
    }

    override fun setUI() {
        binding.apply {
            loadingFull.root.isVisible = true
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
            setToolbarText(R.string.nav_dualism)
        }
    }

    override fun setListeners() {
        binding.apply {
            clOptionTop.setOnClickListener { topOrBottomClick(isTop = true) }
            clOptionBottom.setOnClickListener { topOrBottomClick(isTop = false) }

            tvCreatorName.setOnClickListener {
                checkNotNulls(actualData, actualData?.creatorId) { _, creatorId ->
                    viewModel.checkIfIsMe(creatorId)
                }
            }

            btnComment.setOnClickListener { sendComment() }
            btnFav.setOnClickListener { favOnClick() }
            btnShare.setOnClickListener { share() }
            btnMenu.setOnClickListener { openMenu() }

            tvNext.setOnClickListener {
                position++
                if (position < dualismList.size) {
//                    showInterstitial()
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

    private fun topOrBottomClick(isTop: Boolean) {
        binding.apply {
            actualData?.let { data ->
                tvNext.isEnabled = false
                viewModel.voteDualism(data.id, isTop)
                val green = getColor(mContext, R.color.color_green_top)
                if (isTop) clOptionTop.backgroundTintList = valueOf(green)
                else clOptionBottom.backgroundTintList = valueOf(green)

                clOptionTop.isEnabled = false
                clOptionBottom.isEnabled = false
            } ?: run { error() }
        }
    }

    override fun observe() {
        viewModel.event.observe(this) { event ->
            when (event) {
                is DualismsEvent.InitiatedSession -> {
                    hasSession = event.initiatedSession
                    viewModel.getDualisms()
                }

                is DualismsEvent.ReCheckSession -> hasSession = event.initiatedSession

                is DualismsEvent.Dualisms -> {
                    dualismList = event.data
                    setData()
                }

                is DualismsEvent.CheckDualismFav -> {
                    if (event.result) setFavButton(isOn = true)
                    else setFavButton(isOn = false)
                    isFav = event.result
                    setUpComments()
                    actualData?.let { d -> viewModel.getVotes(d.id) }
                }

                is DualismsEvent.GetVotes -> setUpVotes(event.votes)
                is DualismsEvent.GetPercents -> setPercents(event.percents)

                is DualismsEvent.VoteDilemma -> {
                    if (event.wasTop) votesTop++ else votesBottom++
                    viewModel.getPercents(votesTop, votesBottom)
                }

                is DualismsEvent.GetSessionToComment -> openSendCommentSheet(event.session)
                is DualismsEvent.NavigateToProfile -> toUserProfile(event.isMe, event.userId)
                is DualismsEvent.Reported -> reported()
                is DualismsEvent.SWW -> error()
            }
        }
    }

    private fun setData() {
        try {
            binding.apply {
                setInitialConfig()
                actualData = dualismList[position]
                actualData?.let { data ->
                    viewModel.checkIfIsFav(data.id)
                    data.explanation?.let { exp ->
                        tvExplanation.text = exp
                        tvExplanation.isVisible = true
                    } ?: run { tvExplanation.isVisible = false }
                    tvTextTop.text = data.txtTop
                    tvResultTopText.text = data.txtTop
                    tvTextBottom.text = data.txtBottom
                    tvResultBottomText.text = data.txtBottom
                    data.creatorName?.let { name ->
                        tvCreatorName.text = name
                        llCreatorName.isVisible = true
                    } ?: run { llCreatorName.isVisible = false }
                }
            }
        } catch (e: Exception) {
            error()
        }
    }

    private fun setInitialConfig() {
        binding.apply {
            val grey = getColor(mContext, R.color.grey)
            clOptionTop.backgroundTintList = valueOf(grey)
            clOptionBottom.backgroundTintList = valueOf(grey)

            llResultTop.visibility = View.INVISIBLE
            llResultBottom.visibility = View.INVISIBLE

            tvTextTop.animate().alpha(1f).duration = 1000
            llResultTop.animate().alpha(0f).duration = 100
            tvTextBottom.animate().alpha(1f).duration = 1000
            llResultBottom.animate().alpha(0f).duration = 100

            clOptionTop.isEnabled = true
            clOptionBottom.isEnabled = true
        }
//        if (::commentsFragment.isInitialized) commentsFragment.clearData()
    }

    private fun setFavButton(isOn: Boolean) {
        binding.apply {
            btnFav.apply {
                imageTintList = if (isOn) {
                    tag = FavButtonTag.FAV
                    isFav = true
                    animate().rotation(720f).setDuration(350).start()
                    setImageResource(R.drawable.ic_fav_on)
                    valueOf(getColor(mContext, R.color.saved))
                } else {
                    tag = FavButtonTag.NO_FAV
                    isFav = false
                    animate().rotation(0f).setDuration(350).start()
                    setImageResource(R.drawable.ic_fav_off)
                    valueOf(getColor(mContext, R.color.black))
                }
            }
        }
    }

    private fun setUpComments() {
        actualData?.let { d ->
            commentsFragment = CommentsFragment(d.id, DUALISM, this@DualismsFragment)
            childFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, commentsFragment)
                .commit()
        }
    }

    private fun setUpVotes(votes: DualismVotes) {
        binding.apply {
            votesTop = votes.votesTop
            votesBottom = votes.votesBottom
            loadingFull.root.isVisible = false
        }
    }

    private fun setPercents(actualPercents: Percents) {
        binding.apply {
            tvPercentsTop.text = actualPercents.percentYesTop
            tvPercentsBottom.text = actualPercents.percentNoBottom
            tvVotesTop.text = getString(R.string.dashboard_dualism_votes, votesTop.toString())
            tvVotesBottom.text = getString(R.string.dashboard_dualism_votes, votesBottom.toString())

            llResultTop.visibility = View.VISIBLE
            llResultBottom.visibility = View.VISIBLE

            tvTextTop.animate().alpha(0f).duration = 100
            llResultTop.animate().alpha(1f).duration = 1000
            tvTextBottom.animate().alpha(0f).duration = 100
            llResultBottom.animate().alpha(1f).duration = 1000

            countDown(500) { tvNext.isEnabled = true }
        }
    }

    private fun openMenu() {
        val dialog = MenuDashboardBSheet(isFav, this@DualismsFragment)
        activity?.let { dialog.show(it.supportFragmentManager, "") }
    }

    override fun shouldInitiateSession() {
        localBroadcastOpenLogin()
    }

    private fun localBroadcastOpenLogin() =
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(Intent(LOGIN_BROADCAST))

    override fun navigateToUserProfile(userId: String) = viewModel.checkIfIsMe(userId)

    private fun toUserProfile(isMe: Boolean, userId: String) {
        val navGraph = if (isMe) R.navigation.nav_graph_profile
        else R.navigation.nav_graph_other_profile
        (activity as BedRockActivity).openActivity(navGraph, USER_ID, userId)
    }

    override fun sendComment() = viewModel.getSessionToComment()

    private fun openSendCommentSheet(session: Session?) {
        session?.let { userData ->
            actualData?.let { dualism ->
                val dialog = SendCommentBSheet(dualism, DUALISM, userData, this@DualismsFragment)
                activity?.let { dialog.show(it.supportFragmentManager, "") }
            }
        } ?: run { localBroadcastOpenLogin() }
    }

    override fun commentSent(comment: Comment) {
        if (::commentsFragment.isInitialized) commentsFragment.commentSent(comment)
    }

    override fun setFavorite() = favOnClick()

    private fun favOnClick() {
        if (hasSession) {
            binding.btnFav.apply {
                actualData?.let { data ->
                    when (tag) {
                        FavButtonTag.FAV -> {
                            setFavButton(isOn = false)
                            viewModel.deleteFavDualism(data.id)
                        }

                        FavButtonTag.NO_FAV -> {
                            setFavButton(isOn = true)
                            viewModel.setDualismFav(data.id, data.txtTop, data.txtBottom)
                        }

                        else -> Log.e("FavButton", "Unexpected click")
                    }
                } ?: run { error() }
            }
        } else localBroadcastOpenLogin()
    }

    override fun copyText() {
        checkNotNulls(activity, actualData) { a, data ->
            val clipboard = a.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val text = "${data.txtTop} ${getString(R.string.dashboard_or)} ${data.txtBottom}"
            val clip = ClipData.newPlainText("label", text)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(
                mContext,
                getString(R.string.menu_dashboard_dualism_copied),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun share() {
        actualData?.let { data ->
            val text = "${data.txtTop} ${getString(R.string.dashboard_or)} ${data.txtBottom}, " +
                    "${getString(R.string.dashboard_share_which_take)} \n\n" +
                    "${getString(R.string.dashboard_share_more_in)}\n" +
                    getString(R.string.dashboard_share_url)
            val shareIntent = shareContent(text)
            startActivity(shareIntent)
        }
    }

    override fun report() {
        actualData?.let { data -> viewModel.reportDualism(data) }
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

