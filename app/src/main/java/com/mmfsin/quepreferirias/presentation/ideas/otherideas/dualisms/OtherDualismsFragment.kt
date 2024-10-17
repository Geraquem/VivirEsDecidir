package com.mmfsin.quepreferirias.presentation.ideas.otherideas.dualisms

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
import com.mmfsin.quepreferirias.domain.models.SendDualism
import com.mmfsin.quepreferirias.presentation.ideas.interfaces.IIdeasListener
import com.mmfsin.quepreferirias.presentation.ideas.otherideas.dualisms.adapter.OtherDualismsAdapter
import com.mmfsin.quepreferirias.presentation.ideas.otherideas.dualisms.interfaces.IOtherDualismsListener
import com.mmfsin.quepreferirias.utils.showErrorDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OtherDualismsFragment(val userId: String, val listener: IIdeasListener) :
    BaseFragment<FragmentRvDataBinding, OtherDualismsViewModel>(), IOtherDualismsListener {

    override val viewModel: OtherDualismsViewModel by viewModels()
    private lateinit var mContext: Context


    override fun inflateView(
        inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentRvDataBinding.inflate(inflater, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getUserDualisms(userId)
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
                is OtherDualismsEvent.Dualisms -> setUserDualisms(event.data)
                is OtherDualismsEvent.SWW -> error()
            }
        }
    }

    private fun setUserDualisms(dualisms: List<SendDualism>) {
        binding.apply {
            val visible = dualisms.isEmpty()
            tvEmpty.isVisible = visible
            rvData.apply {
                layoutManager = LinearLayoutManager(mContext)
                adapter = OtherDualismsAdapter(dualisms, this@OtherDualismsFragment)
            }
            rvData.isVisible = !visible
            loading.root.visibility = View.GONE
        }
    }

    override fun onDualismClick(dualismId: String) = listener.navigateToSingleDualism(dualismId)

    private fun error() = activity?.showErrorDialog { activity?.finish() }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
}