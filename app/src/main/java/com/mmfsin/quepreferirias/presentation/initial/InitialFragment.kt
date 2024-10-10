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
import com.mmfsin.quepreferirias.utils.animateY
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
        viewModel.checkSessionInitiated()
    }

    override fun setUI() {
        binding.apply {
            btnLogin.root.visibility = View.INVISIBLE
            btnLogin.root.animateY(-500f, 10)
            llHello.visibility = View.INVISIBLE
            llHello.animateY(-500f, 10)
        }
    }

    override fun setListeners() {
        binding.apply {
        }
    }

    override fun observe() {
        viewModel.event.observe(this) { event ->
            when (event) {
                is InitialEvent.InitiatedSession -> {
                    hasSession = event.initiatedSession
                    setUserName()
                    if (hasSession) viewModel.getSession()
                }

                is InitialEvent.ReCheckSession -> {}
                is InitialEvent.GetSession -> {
                    userData = event.session
                    setUserName()
                }

                is InitialEvent.SWW -> error()
            }
        }
    }

    private fun setUserName() {
        binding.apply {
            if (hasSession) {
                llHello.visibility = View.VISIBLE
                llHello.animateY(0f, 1000)
            } else {
                btnLogin.root.visibility = View.VISIBLE
                btnLogin.root.animateY(0f, 1000)
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

    override fun onResume() {
        super.onResume()
        viewModel.reCheckSession()
    }
}