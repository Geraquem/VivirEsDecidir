package com.mmfsin.quepreferirias.presentation.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.mmfsin.quepreferirias.R
import com.mmfsin.quepreferirias.base.BaseFragment
import com.mmfsin.quepreferirias.databinding.FragmentProfileBinding
import com.mmfsin.quepreferirias.domain.models.Session
import com.mmfsin.quepreferirias.presentation.main.BedRockActivity
import com.mmfsin.quepreferirias.utils.showErrorDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding, ProfileViewModel>() {

    override val viewModel: ProfileViewModel by viewModels()
    private lateinit var mContext: Context

    private var session: Session? = null

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
                loading.root.visibility = View.GONE
            }
        }
    }

    override fun setListeners() {
        binding.apply {
            tvCloseSession.setOnClickListener { viewModel.closeSession() }
        }
    }

    override fun observe() {
        viewModel.event.observe(this) { event ->
            when (event) {
                is ProfileEvent.Profile -> {
                    session = event.session
                    setUI()
                    /** viewmodel ver mi actividad */
                }

                is ProfileEvent.SessionClosed -> activity?.finish()
                is ProfileEvent.SWW -> error()
            }
        }
    }

    private fun error() = activity?.showErrorDialog() { activity?.finish() }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
}