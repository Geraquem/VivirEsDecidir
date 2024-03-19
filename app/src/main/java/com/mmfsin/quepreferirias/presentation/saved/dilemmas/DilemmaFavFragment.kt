package com.mmfsin.quepreferirias.presentation.saved.dilemmas

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mmfsin.quepreferirias.base.BaseFragment
import com.mmfsin.quepreferirias.databinding.FragmentSavedDataBinding
import com.mmfsin.quepreferirias.domain.models.DilemmaFav
import com.mmfsin.quepreferirias.presentation.saved.adapter.DilemmaFavsAdapter
import com.mmfsin.quepreferirias.presentation.saved.interfaces.IDilemmaFavListener
import com.mmfsin.quepreferirias.utils.showErrorDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DilemmaFavFragment : BaseFragment<FragmentSavedDataBinding, DilemmaFavViewModel>(),
    IDilemmaFavListener {

    override val viewModel: DilemmaFavViewModel by viewModels()
    private lateinit var mContext: Context

    private var hasSession = false

    override fun inflateView(
        inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentSavedDataBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.checkSessionInitiated()
    }

    override fun setUI() {
        binding.apply {
            loading.root.isVisible
        }
    }

    override fun setListeners() {
        binding.apply {
        }
    }

    override fun observe() {
        viewModel.event.observe(this) { event ->
            when (event) {
                is DilemmaFavDataEvent.InitiatedSession -> {
                    hasSession = event.initiatedSession
                    if (hasSession) viewModel.getFavData()
                }

                is DilemmaFavDataEvent.Data -> setDilemmaFavs(event.data)
                is DilemmaFavDataEvent.SWW -> error()
            }
        }
    }

    private fun setDilemmaFavs(dilemmas: List<DilemmaFav>) {
        binding.apply {
            val visible = dilemmas.isEmpty()
            tvEmpty.isVisible = visible
            rvSavedData.apply {
                layoutManager = LinearLayoutManager(mContext)
                adapter = DilemmaFavsAdapter(dilemmas, this@DilemmaFavFragment)
            }
            rvSavedData.isVisible = !visible
            loading.root.visibility = View.GONE
        }
    }

    override fun onDilemmaFavClick(dilemmaId: String) {
        Toast.makeText(mContext, dilemmaId, Toast.LENGTH_SHORT).show()
    }

    private fun error() {
        activity?.showErrorDialog { activity?.finish() }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
}