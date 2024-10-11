package com.mmfsin.quepreferirias.presentation.ideas.myideas.dualisms

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
import com.mmfsin.quepreferirias.presentation.ideas.myideas.dialogs.DeleteMyDataDialog
import com.mmfsin.quepreferirias.presentation.ideas.myideas.dualisms.adapter.MyDualismsAdapter
import com.mmfsin.quepreferirias.presentation.ideas.myideas.dualisms.interfaces.IMyDualismsListener
import com.mmfsin.quepreferirias.presentation.models.DashboardType.DILEMMA
import com.mmfsin.quepreferirias.utils.showErrorDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyDualismsFragment(val listener: IIdeasListener) :
    BaseFragment<FragmentRvDataBinding, MyDualismsViewModel>(), IMyDualismsListener {

    override val viewModel: MyDualismsViewModel by viewModels()
    private lateinit var mContext: Context

    private var dialog: DeleteMyDataDialog? = null

    override fun inflateView(
        inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentRvDataBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getMyDualisms()
    }

    override fun setUI() {
        binding.apply {
            loading.root.isVisible
            swipe.isEnabled = false
            tvEmpty.text = getString(R.string.my_data_empty)
        }
    }

    override fun observe() {
        viewModel.event.observe(this) { event ->
            when (event) {
                is MyDualismsEvent.Dualisms -> setMyDualisms(event.data)
                is MyDualismsEvent.Deleted -> {
                    dialog?.dismiss()
                    viewModel.getMyDualisms()
                }

                is MyDualismsEvent.SWW -> error()
            }
        }
    }

    private fun setMyDualisms(dualisms: List<SendDualism>) {
        binding.apply {
            val visible = dualisms.isEmpty()
            tvEmpty.isVisible = visible
            rvData.apply {
                layoutManager = LinearLayoutManager(mContext)
                adapter = MyDualismsAdapter(dualisms, this@MyDualismsFragment)
            }
            rvData.isVisible = !visible
            loading.root.visibility = View.GONE
        }
    }

    override fun onMyDualismClick(dualismId: String) {
        listener.navigateToSingleDualism(dualismId)
    }

    override fun onMyDualismLongClick(dualismId: String) {
        dialog = DeleteMyDataDialog(type = DILEMMA) { viewModel.deleteMyDualism(dualismId) }
        activity?.let { dialog?.show(it.supportFragmentManager, "") }
    }

    private fun error() {
        activity?.showErrorDialog { activity?.finish() }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
}