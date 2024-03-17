package com.mmfsin.quepreferirias.presentation.send.dialogs

import android.app.Dialog
import android.view.LayoutInflater
import androidx.navigation.NavGraph
import com.mmfsin.quepreferirias.base.BaseDialog
import com.mmfsin.quepreferirias.databinding.DialogSendDataBinding
import com.mmfsin.quepreferirias.presentation.send.interfaces.IBSheetSendData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SendDataDialog(navigate: (navGraph: Int) -> Unit) : BaseDialog<DialogSendDataBinding>() {

    override fun inflateView(inflater: LayoutInflater) = DialogSendDataBinding.inflate(inflater)

    override fun setUI() {
        isCancelable = true
    }

    override fun setCustomViewDialog(dialog: Dialog) = bottomViewDialog(dialog)

    override fun setListeners() {
        binding.apply {
            ivClose.setOnClickListener { dismiss() }
            sendDilemma.setOnClickListener {  }
            sendDualism.setOnClickListener {  }
        }
    }
}