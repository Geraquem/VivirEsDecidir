package com.mmfsin.quepreferirias.presentation.ideas.otherideas.dilemmas

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mmfsin.quepreferirias.R
import com.mmfsin.quepreferirias.base.BaseFragment
import com.mmfsin.quepreferirias.databinding.FragmentRvDataBinding
import com.mmfsin.quepreferirias.domain.models.SendDilemma
import com.mmfsin.quepreferirias.presentation.ideas.interfaces.IIdeasListener
import com.mmfsin.quepreferirias.presentation.ideas.otherideas.dilemmas.adapter.OtherDilemmasAdapter
import com.mmfsin.quepreferirias.presentation.ideas.otherideas.dilemmas.interfaces.IOtherDilemmasListener
import com.mmfsin.quepreferirias.utils.showErrorDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OtherDilemmasFragment(val userId: String, val listener: IIdeasListener) :
    BaseFragment<FragmentRvDataBinding, OtherDilemmasViewModel>(), IOtherDilemmasListener {

    override val viewModel: OtherDilemmasViewModel by viewModels()
    private lateinit var mContext: Context


    override fun inflateView(
        inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentRvDataBinding.inflate(inflater, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getUserDilemmas(userId)
    }

    override fun setUI() {
        binding.apply {
            loading.root.isVisible
            swipe.isEnabled = false
            tvEmpty.text = getString(R.string.other_data_empty)
        }
    }

    override fun observe() {
        viewModel.event.observe(this) { event ->
            when (event) {
                is OtherDilemmasEvent.Dilemmas -> setUserDilemmas(event.data)
                is OtherDilemmasEvent.SWW -> error()
            }
        }
    }

    private fun setUserDilemmas(dilemmas: List<SendDilemma>) {
        binding.apply {
            val visible = dilemmas.isEmpty()
            tvEmpty.isVisible = visible
            rvData.apply {
                layoutManager = LinearLayoutManager(mContext)
                adapter = OtherDilemmasAdapter(dilemmas, this@OtherDilemmasFragment)
            }
            rvData.isVisible = !visible
            loading.root.visibility = View.GONE
        }
    }

    override fun onDilemmaClick(dilemmaId: String) = listener.navigateToSingleDilemma(dilemmaId)

    private fun error() = activity?.showErrorDialog { activity?.finish() }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
}