package com.mmfsin.quepreferirias.presentation.dashboard.common.dialog

import android.app.Dialog
import android.view.LayoutInflater
import com.mmfsin.quepreferirias.base.BaseDialog
import com.mmfsin.quepreferirias.databinding.DialogNoMoreBinding

class NoMoreDialog(val action: () -> Unit?) : BaseDialog<DialogNoMoreBinding>() {

    override fun inflateView(inflater: LayoutInflater) = DialogNoMoreBinding.inflate(inflater)

    override fun setCustomViewDialog(dialog: Dialog) = centerCustomViewDialog(dialog)

    override fun setListeners() {
        binding.btnAccept.setOnClickListener {
            action()
            dismiss()
        }
    }
}