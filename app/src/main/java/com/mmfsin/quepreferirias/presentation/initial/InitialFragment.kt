package com.mmfsin.quepreferirias.presentation.initial

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.mmfsin.quepreferirias.base.BaseFragment
import com.mmfsin.quepreferirias.databinding.FragmentInitialBinding
import com.mmfsin.quepreferirias.utils.showErrorDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InitialFragment : BaseFragment<FragmentInitialBinding, InitialViewModel>() {

    override val viewModel: InitialViewModel by viewModels()

    private lateinit var mContext: Context

    override fun inflateView(
        inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentInitialBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun setUI() {
        binding.apply {
        }
    }

    override fun setListeners() {
        binding.apply {
        }
    }

    override fun observe() {
        viewModel.event.observe(this) { event ->
            when (event) {
                is InitialEvent.SWW -> error()
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