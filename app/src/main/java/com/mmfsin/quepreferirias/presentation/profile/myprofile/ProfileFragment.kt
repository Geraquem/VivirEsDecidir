package com.mmfsin.quepreferirias.presentation.profile.myprofile

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import com.mmfsin.quepreferirias.databinding.FragmentProfileBinding
import com.mmfsin.quepreferirias.domain.models.RRSS
import com.mmfsin.quepreferirias.domain.models.RRSSType
import com.mmfsin.quepreferirias.domain.models.RRSSType.*
import com.mmfsin.quepreferirias.domain.models.Session
import com.mmfsin.quepreferirias.presentation.main.BedRockActivity
import com.mmfsin.quepreferirias.presentation.profile.common.adapter.RRSSAdapter
import com.mmfsin.quepreferirias.presentation.profile.common.listeners.IRRSSListener
import com.mmfsin.quepreferirias.presentation.profile.myprofile.ProfileFragmentDirections.Companion.actionToMyData
import com.mmfsin.quepreferirias.presentation.profile.myprofile.ProfileFragmentDirections.Companion.actionToSavedData
import com.mmfsin.quepreferirias.presentation.profile.myprofile.dialogs.CloseSessionDialog
import com.mmfsin.quepreferirias.presentation.profile.myprofile.dialogs.RRSSDialog
import com.mmfsin.quepreferirias.utils.showErrorDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding, ProfileViewModel>(), IRRSSListener {

    override val viewModel: ProfileViewModel by viewModels()
    private lateinit var mContext: Context

    private var session: Session? = null
    private var closeSessionDialog: CloseSessionDialog? = null
    private var updatedProfileDialog: RRSSDialog? = null

    override fun inflateView(
        inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentProfileBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getSession()
    }

    private fun setToolbar() {
        (activity as BedRockActivity).apply {
            backListener { onBackPressed() }
            setToolbarText(R.string.profile_title)
        }
    }

    override fun setUI() {
        setToolbar()
        binding.apply {
            loading.root.visibility = View.VISIBLE
            session?.let {
                Glide.with(mContext).load(it.imageUrl).into(ivImage.image)
                tvName.text = it.name
                it.rrss?.let { rrss -> setRRSS(rrss) }
            }
        }
    }

    override fun setListeners() {
        binding.apply {
            tvAddRrss.setOnClickListener { openEditDialog() }

            llMyIdeas.setOnClickListener { findNavController().navigate(actionToMyData()) }
            llMyFavs.setOnClickListener { findNavController().navigate(actionToSavedData()) }

            tvEdit.setOnClickListener { openEditDialog() }
            tvCloseSession.setOnClickListener {
                closeSessionDialog = CloseSessionDialog { viewModel.closeSession() }
                activity?.let { closeSessionDialog?.show(it.supportFragmentManager, "") }
            }
        }
    }

    private fun openEditDialog() {
        session?.let { data ->
            updatedProfileDialog = RRSSDialog(data, this@ProfileFragment)
            activity?.let { updatedProfileDialog?.show(it.supportFragmentManager, "") }
        }
    }

    override fun observe() {
        viewModel.event.observe(this) { event ->
            when (event) {
                is ProfileEvent.Profile -> {
                    session = event.session
                    setUI()
                }

                is ProfileEvent.UpdatedProfile -> {
                    viewModel.getSession()
                    updatedProfileDialog?.dismiss()
                }

                is ProfileEvent.SessionClosed -> {
                    activity?.finish()
                    closeSessionDialog?.dismiss()
                }

                is ProfileEvent.SWW -> error()
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
            tvAddRrss.isVisible = data.isEmpty()
            llBgRrss.isVisible = data.isNotEmpty()
            rvRrss.apply {
                layoutManager = LinearLayoutManager(mContext)
                adapter = RRSSAdapter(data, this@ProfileFragment)
            }
            loading.root.visibility = View.GONE
        }
    }

    override fun updateRRSS(rrss: RRSS) = viewModel.updateRRSS(rrss)

    override fun onRRSSClick(type: RRSSType, name: String) {
        val url = when (type) {
            INSTAGRAM -> getString(R.string.instagram_url, name)
            TWITTER -> getString(R.string.twitter_url, name)
            TIKTOK -> getString(R.string.tiktok_url, name)
            YOUTUBE -> getString(R.string.youtube_url, name)
        }
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    private fun error() = activity?.showErrorDialog() { activity?.finish() }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
}