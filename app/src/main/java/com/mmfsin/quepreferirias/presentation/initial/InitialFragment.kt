package com.mmfsin.quepreferirias.presentation.initial

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.mmfsin.quepreferirias.R
import com.mmfsin.quepreferirias.base.BaseFragment
import com.mmfsin.quepreferirias.databinding.FragmentInitialAuxBinding
import com.mmfsin.quepreferirias.presentation.main.MainActivity
import com.mmfsin.quepreferirias.utils.animateX
import com.mmfsin.quepreferirias.utils.animateY
import com.mmfsin.quepreferirias.utils.countDown
import com.mmfsin.quepreferirias.utils.showAlpha
import com.mmfsin.quepreferirias.utils.showErrorDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InitialFragment : BaseFragment<FragmentInitialAuxBinding, InitialViewModel>() {

    override val viewModel: InitialViewModel by viewModels()

    private lateinit var mContext: Context

    override fun inflateView(
        inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentInitialAuxBinding.inflate(inflater, container, false)

    override fun setUI() {
        binding.apply {
            loading.root.isVisible = true
            bgTop.animateY(-500f, 10)
            tvQuestion.showAlpha(0f, 10)
            llDescriptions.showAlpha(0f, 10)
            btnDilemmas.animateX(-500f, 10)
            btnDualisms.animateX(500f, 10)
            buttons.root.animateY(500f, 10)

            countDown(1200) {
                loading.root.isVisible = false
                startAnimations()
            }
        }
    }

    private fun startAnimations() {
        binding.apply {
            bgTop.animateY(0f, 500)
            countDown(750) {
                btnDilemmas.animateX(0f, 500)
                btnDualisms.animateX(0f, 500)
                tvQuestion.showAlpha(1f, 500)
                llDescriptions.showAlpha(1f, 500)
                countDown(500) {
                    buttons.root.animateY(0f, 500)
                }
            }
        }
    }

    override fun setListeners() {
        binding.apply {
            btnDilemmas.setOnClickListener { navigate(R.navigation.nav_graph_dilemmas) }
            btnDualisms.setOnClickListener { navigate(R.navigation.nav_graph_dualisms) }

            buttons.apply {
                btnMenu.setOnClickListener { (activity as MainActivity).openDrawer() }
                btnProfile.setOnClickListener { (activity as MainActivity).navigateToUserProfileFromFragment() }
                btnFavs.setOnClickListener { (activity as MainActivity).navigateToUserFavoritesFragment() }
                btnSendData.setOnClickListener { (activity as MainActivity).openSendDataDialog() }
            }
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