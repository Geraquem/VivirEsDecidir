package com.mmfsin.quepreferirias.presentation.dashboard.dualisms

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView.OnScrollChangeListener
import androidx.fragment.app.viewModels
import com.mmfsin.quepreferirias.R
import com.mmfsin.quepreferirias.base.BaseFragment
import com.mmfsin.quepreferirias.databinding.FragmentDualismBinding
import com.mmfsin.quepreferirias.domain.models.Dilemma
import com.mmfsin.quepreferirias.presentation.dashboard.dilemmas.comments.CommentsFragment
import com.mmfsin.quepreferirias.presentation.main.BedRockActivity
import com.mmfsin.quepreferirias.utils.checkNotNulls
import com.mmfsin.quepreferirias.utils.showErrorDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DualismsFragment : BaseFragment<FragmentDualismBinding, DualismsViewModel>() {

    override val viewModel: DualismsViewModel by viewModels()
    private lateinit var mContext: Context

    private var hasSession = false

    private var dualismsList = emptyList<Dilemma>()
    private var actualData: Dilemma? = null
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
//            setInitialConfig()

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

//            btnComment.setOnClickListener { sendComment() }
//            btnFav.setOnClickListener { favOnClick() }
//            btnShare.setOnClickListener { share() }
//            btnMenu.setOnClickListener { openMenu() }

//            tvNext.setOnClickListener {
//                position++
//                if (position < dualismsList.size) {
////                    showInterstitial()
//                    setInitialConfig()
//                    setData()
//                } else {
//                    activity?.let {
//                        val dialog = NoMoreDialog {
//                            activity?.onBackPressedDispatcher?.onBackPressed()
//                        }
//                        dialog.show(it.supportFragmentManager, "")
//                    }
//                }
//            }
        }
    }

    override fun observe() {
        viewModel.event.observe(this) { event ->
            when (event) {
                is DualismsEvent.InitiatedSession -> {
                    hasSession = event.initiatedSession
//                    viewModel.getDilemmas()
                }

                is DualismsEvent.ReCheckSession -> hasSession = event.initiatedSession


                is DualismsEvent.SWW -> error()
            }
        }
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

