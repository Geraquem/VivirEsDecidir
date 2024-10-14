package com.mmfsin.quepreferirias.presentation.initial

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.mmfsin.quepreferirias.base.BaseFragment
import com.mmfsin.quepreferirias.databinding.FragmentInitialBinding
import com.mmfsin.quepreferirias.domain.models.Session
import com.mmfsin.quepreferirias.presentation.main.MainActivity
import com.mmfsin.quepreferirias.utils.showErrorDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InitialFragment : BaseFragment<FragmentInitialBinding, InitialViewModel>() {

    override val viewModel: InitialViewModel by viewModels()

    private lateinit var mContext: Context

    private var hasSession: Boolean = false
    private var userData: Session? = null

    override fun inflateView(
        inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentInitialBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        viewModel.checkSessionInitiated()
    }

    override fun setUI() {
        binding.apply {
        }
    }

    override fun setListeners() {
        binding.apply {
            btnOpenMenu.setOnClickListener { (activity as MainActivity).openDrawer() }

            buttons.btnSend.setOnClickListener { (activity as MainActivity).openSendDataDialog() }
            buttons.btnProfile.setOnClickListener { (activity as MainActivity).navigateToUserProfileFromFragment() }
            buttons.btnFavs.setOnClickListener { (activity as MainActivity).navigateToUserFavoritesFragment() }

//            llDilemmas.setOnClickListener { navigate(R.navigation.nav_graph_dilemmas) }
//            llDualisms.setOnClickListener { navigate(R.navigation.nav_graph_dualisms) }
        }
    }

    private fun navigate(navGraph: Int) = (activity as MainActivity).navigateTo(navGraph)

    override fun observe() {
        viewModel.event.observe(this) { event ->
            when (event) {
                is InitialEvent.InitiatedSession -> {
//                    hasSession = event.initiatedSession
//                    setUserName()
//                    if (hasSession) viewModel.getSession()
                }

                is InitialEvent.ReCheckSession -> {
//                    hasSession = event.initiatedSession
//                    val a = 2
                }

                is InitialEvent.GetSession -> {
//                    binding.tvUserName.text = event.session?.name
//                    setUserName()
                }

                is InitialEvent.SWW -> error()
            }
        }
    }

    private fun setUserName() {
        binding.apply {

        }
    }

    private fun error() {
        activity?.showErrorDialog { activity?.finish() }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onResume() {
        super.onResume()
//        viewModel.checkSessionInitiated()
    }
}