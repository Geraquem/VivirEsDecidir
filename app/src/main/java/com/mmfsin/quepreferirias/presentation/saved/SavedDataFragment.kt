package com.mmfsin.quepreferirias.presentation.saved

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.mmfsin.quepreferirias.R
import com.mmfsin.quepreferirias.base.BaseFragment
import com.mmfsin.quepreferirias.databinding.FragmentDilemmaBinding
import com.mmfsin.quepreferirias.databinding.FragmentSavedDataBinding
import com.mmfsin.quepreferirias.presentation.main.BedRockActivity
import com.mmfsin.quepreferirias.utils.showErrorDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SavedDataFragment : BaseFragment<FragmentSavedDataBinding, SavedDataViewModel>() {

    override val viewModel: SavedDataViewModel by viewModels()
    private lateinit var mContext: Context

    private var hasSession = false

    override fun inflateView(
        inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentSavedDataBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        viewModel.checkSessionInitiated()
    }

    override fun setUI() {
        binding.apply {
            loading.root.isVisible
            setToolbar()
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
        }
    }


    override fun observe() {
        viewModel.event.observe(this) { event ->
            when (event) {
                is SavedDataEvent.InitiatedSession -> {
                    hasSession = event.initiatedSession
//                    viewModel.getDilemmas()
                }

                is SavedDataEvent.Data -> {}
                is SavedDataEvent.SWW -> error()
            }
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