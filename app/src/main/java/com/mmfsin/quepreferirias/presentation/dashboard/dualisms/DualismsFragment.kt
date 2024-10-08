package com.mmfsin.quepreferirias.presentation.dashboard.dualisms

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView.OnScrollChangeListener
import androidx.fragment.app.viewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.mmfsin.quepreferirias.R
import com.mmfsin.quepreferirias.base.BaseFragment
import com.mmfsin.quepreferirias.databinding.FragmentDualismBinding
import com.mmfsin.quepreferirias.domain.models.Dualism
import com.mmfsin.quepreferirias.domain.models.DualismVotes
import com.mmfsin.quepreferirias.presentation.dashboard.dilemmas.comments.CommentsFragment
import com.mmfsin.quepreferirias.presentation.dashboard.dilemmas.interfaces.ICommentsListener
import com.mmfsin.quepreferirias.presentation.main.BedRockActivity
import com.mmfsin.quepreferirias.presentation.models.FavButtonTag
import com.mmfsin.quepreferirias.utils.LOGIN_BROADCAST
import com.mmfsin.quepreferirias.utils.USER_ID
import com.mmfsin.quepreferirias.utils.checkNotNulls
import com.mmfsin.quepreferirias.utils.showErrorDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DualismsFragment : BaseFragment<FragmentDualismBinding, DualismsViewModel>(),
    ICommentsListener {

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
            tvCreatorName.setOnClickListener {
                checkNotNulls(actualData, actualData?.creatorId) { _, creatorId ->
//                    viewModel.checkIfIsMe(creatorId)
                }
            }
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

                is DualismsEvent.NavigateToProfile -> toUserProfile(event.isMe, event.userId)
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
                    tvTextBottom.text = data.txtBottom
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
            llResultTop.isVisible = false
            llResultBottom.isVisible = false
        }
    }

    private fun setFavButton(isOn: Boolean) {
        binding.apply {
            btnFav.apply {
                imageTintList = if (isOn) {
                    tag = FavButtonTag.FAV
                    isFav = true
                    animate().rotation(720f).setDuration(350).start()
                    setImageResource(R.drawable.ic_fav_on)
                    ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.saved))
                } else {
                    tag = FavButtonTag.NO_FAV
                    isFav = false
                    animate().rotation(0f).setDuration(350).start()
                    setImageResource(R.drawable.ic_fav_off)
                    ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.black))
                }
            }
        }
    }

    private fun setUpComments() {
        actualData?.let { d ->
            commentsFragment = CommentsFragment(d.id, this@DualismsFragment)
            childFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, commentsFragment)
                .commit()
        }
    }

    private fun setUpVotes(votes: DualismVotes) {
        binding.apply {
//            votesTop= votes.votesTop
            votesTop = 35665
//            votesBottom = votes.votesBottom
            votesBottom = 9856
            loadingFull.root.isVisible = false
        }
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

