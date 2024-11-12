package com.mmfsin.quepreferirias.presentation.initial

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.mmfsin.quepreferirias.R
import com.mmfsin.quepreferirias.base.BaseFragment
import com.mmfsin.quepreferirias.databinding.FragmentInitialBinding
import com.mmfsin.quepreferirias.presentation.main.MainActivity
import com.mmfsin.quepreferirias.utils.showErrorDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InitialFragment : BaseFragment<FragmentInitialBinding, InitialViewModel>() {

    override val viewModel: InitialViewModel by viewModels()

    private lateinit var mContext: Context

    override fun inflateView(
        inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentInitialBinding.inflate(inflater, container, false)

    override fun setListeners() {
        binding.apply {
            buttons.btnProfile.setOnClickListener { (activity as MainActivity).navigateToUserProfileFromFragment() }
            buttons.btnMenu.setOnClickListener { (activity as MainActivity).openDrawer() }

//            buttons.btnFavs.setOnClickListener { (activity as MainActivity).navigateToUserFavoritesFragment() }
//            buttons.btnSend.setOnClickListener { (activity as MainActivity).openSendDataDialog() }

            openDilemmas.setOnClickListener { navigate(R.navigation.nav_graph_dilemmas) }
            openDualisms.setOnClickListener { navigate(R.navigation.nav_graph_dualisms) }
        }
    }

    private fun navigate(navGraph: Int) = (activity as MainActivity).navigateTo(navGraph)

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