package com.mmfsin.quepreferirias.presentation.send.dialogs

import android.app.Dialog
import android.view.LayoutInflater
import com.mmfsin.quepreferirias.R
import com.mmfsin.quepreferirias.base.BaseDialog
import com.mmfsin.quepreferirias.databinding.DialogSendDataBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SendDataDialog(val navigate: (navGraph: Int) -> Unit) : BaseDialog<DialogSendDataBinding>() {

    override fun inflateView(inflater: LayoutInflater) = DialogSendDataBinding.inflate(inflater)

    override fun setUI() {
        isCancelable = true
    }

    override fun setCustomViewDialog(dialog: Dialog) = bottomViewDialog(dialog)

    override fun setListeners() {
        binding.apply {
            ivClose.setOnClickListener { dismiss() }
            sendDilemma.setOnClickListener { pressButton(R.navigation.nav_graph_send_dilemma) }
            sendDualism.setOnClickListener { pressButton(R.navigation.nav_graph_profile) }
        }
    }

    private fun pressButton(navGraph: Int) {
        navigate(navGraph)
        dismiss()
    }
}