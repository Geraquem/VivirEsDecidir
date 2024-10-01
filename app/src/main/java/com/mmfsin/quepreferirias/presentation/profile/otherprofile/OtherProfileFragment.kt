package com.mmfsin.quepreferirias.presentation.profile.otherprofile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.mmfsin.quepreferirias.R
import com.mmfsin.quepreferirias.base.BaseFragment
import com.mmfsin.quepreferirias.databinding.FragmentOtherProfileBinding
import com.mmfsin.quepreferirias.domain.models.RRSS
import com.mmfsin.quepreferirias.domain.models.RRSSType
import com.mmfsin.quepreferirias.domain.models.RRSSType.INSTAGRAM
import com.mmfsin.quepreferirias.domain.models.RRSSType.TIKTOK
import com.mmfsin.quepreferirias.domain.models.RRSSType.TWITTER
import com.mmfsin.quepreferirias.domain.models.RRSSType.YOUTUBE
import com.mmfsin.quepreferirias.domain.models.Session
import com.mmfsin.quepreferirias.presentation.main.BedRockActivity
import com.mmfsin.quepreferirias.presentation.profile.common.adapter.RRSSAdapter
import com.mmfsin.quepreferirias.presentation.profile.common.listeners.IRRSSListener
import com.mmfsin.quepreferirias.presentation.profile.otherprofile.OtherProfileFragmentDirections.Companion.otherUserProfileToOtherUserIdeas
import com.mmfsin.quepreferirias.utils.USER_ID
import com.mmfsin.quepreferirias.utils.checkNotNulls
import com.mmfsin.quepreferirias.utils.openRRSS
import com.mmfsin.quepreferirias.utils.showErrorDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OtherProfileFragment : BaseFragment<FragmentOtherProfileBinding, OtherProfileViewModel>(),
    IRRSSListener {

    override val viewModel: OtherProfileViewModel by viewModels()
    private lateinit var mContext: Context

    var userId: String? = null
    private var userData: Session? = null

    override fun getBundleArgs() {
        userId = activity?.intent?.getStringExtra(USER_ID)
    }

    override fun inflateView(
        inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentOtherProfileBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userId?.let { id -> viewModel.getUserById(id) } ?: run { error() }
    }

    private fun setToolbar() {
        (activity as BedRockActivity).apply {
            backListener { onBackPressed() }
        }
    }

    override fun setUI() {
        binding.apply {
            loading.root.visibility = View.VISIBLE
            setToolbar()
            userData?.let { user ->
                Glide.with(mContext).load(user.imageUrl).into(ivImage.image)
                tvName.text = user.name
                tvDilemmasTitle.text = getString(R.string.other_profile_dilemmas, user.name)
                tvDilemmasDesc.text =
                    getString(R.string.other_profile_dilemmas_description, user.name)
                user.rrss?.let { rrss -> setRRSS(rrss) }
            }
        }
    }

    override fun setListeners() {
        binding.apply {
            tvNavigateDilemmas.setOnClickListener {
                checkNotNulls(userId, userData) { id, user ->
                    findNavController().navigate(otherUserProfileToOtherUserIdeas(id))
                }
            }
        }
    }

    override fun observe() {
        viewModel.event.observe(this) { event ->
            when (event) {
                is OtherProfileEvent.Profile -> {
                    userData = event.userData
                    setUI()
                }

                is OtherProfileEvent.SWW -> error()
            }
        }
    }

    private fun setRRSS(rrss: RRSS) {
        val data = mutableListOf<Pair<RRSSType, String>>()
        rrss.instagram?.let { data.add(Pair(INSTAGRAM, it)) }
        rrss.twitter?.let { data.add(Pair(TWITTER, it)) }
        rrss.tiktok?.let { data.add(Pair(TIKTOK, it)) }
        rrss.youtube?.let { data.add(Pair(YOUTUBE, it)) }
        binding.apply {
            llBgRrss.isVisible = data.isNotEmpty()
            rvRrss.apply {
                layoutManager = LinearLayoutManager(mContext)
                adapter = RRSSAdapter(data, this@OtherProfileFragment)
            }
            loading.root.visibility = View.GONE
        }
    }

    override fun updateRRSS(rrss: RRSS) {
        /** Do nothing */
    }

    override fun onRRSSClick(type: RRSSType, name: String) {
        activity?.openRRSS(type, name)
    }

    private fun error() = activity?.showErrorDialog() { activity?.finish() }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
}