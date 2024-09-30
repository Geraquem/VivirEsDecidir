package com.mmfsin.quepreferirias.base.dialog

import android.app.Dialog
import android.view.LayoutInflater
import com.mmfsin.quepreferirias.base.BaseDialog
import com.mmfsin.quepreferirias.databinding.DialogErrorBinding

class ErrorDialog(val action: () -> Unit?) : BaseDialog<DialogErrorBinding>() {

    override fun inflateView(inflater: LayoutInflater) = DialogErrorBinding.inflate(inflater)

    override fun setCustomViewDialog(dialog: Dialog) = centerViewDialog(dialog)

    override fun setListeners() {
        binding.btnBack.setOnClickListener {
            action()
            dismiss()
        }
    }
}