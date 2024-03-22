package com.mmfsin.quepreferirias.presentation.myideas.dilemmas

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mmfsin.quepreferirias.R
import com.mmfsin.quepreferirias.base.BaseFragment
import com.mmfsin.quepreferirias.databinding.FragmentRvDataBinding
import com.mmfsin.quepreferirias.domain.models.SendDilemma
import com.mmfsin.quepreferirias.presentation.myideas.dialogs.DeleteMyDataDialog
import com.mmfsin.quepreferirias.presentation.myideas.dialogs.SentType.DILEMMA
import com.mmfsin.quepreferirias.presentation.myideas.dilemmas.adapter.MyDilemmasAdapter
import com.mmfsin.quepreferirias.presentation.myideas.dilemmas.interfaces.IMyDilemmaListener
import com.mmfsin.quepreferirias.utils.showErrorDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyDilemmasFragment : BaseFragment<FragmentRvDataBinding, MyDilemmasViewModel>(),
    IMyDilemmaListener {

    override val viewModel: MyDilemmasViewModel by viewModels()
    private lateinit var mContext: Context

    private var dialog: DeleteMyDataDialog? = null

    override fun inflateView(
        inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentRvDataBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getMyDilemmas()
    }

    override fun setUI() {
        binding.apply {
            loading.root.isVisible
            tvEmpty.text = getString(R.string.my_data_empty)
        }
    }

    override fun observe() {
        viewModel.event.observe(this) { event ->
            when (event) {
                is MyDilemmasEvent.Data -> setMyDilemmas(event.data)
                is MyDilemmasEvent.Deleted -> {
                    dialog?.dismiss()
                    viewModel.getMyDilemmas()
                }

                is MyDilemmasEvent.SWW -> error()
            }
        }
    }

    private fun setMyDilemmas(dilemmas: List<SendDilemma>) {
        binding.apply {
            val visible = dilemmas.isEmpty()
            tvEmpty.isVisible = visible
            rvData.apply {
                layoutManager = LinearLayoutManager(mContext)
                adapter = MyDilemmasAdapter(dilemmas, this@MyDilemmasFragment)
            }
            rvData.isVisible = !visible
            loading.root.visibility = View.GONE
        }
    }

    override fun onMyDilemmaClick(dilemmaId: String) {
        Toast.makeText(mContext, dilemmaId, Toast.LENGTH_SHORT).show()
    }

    override fun onMyDilemmaLongClick(dilemmaId: String) {
        dialog = DeleteMyDataDialog(type = DILEMMA) { viewModel.deleteMyDilemma(dilemmaId) }
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