package com.mmfsin.quepreferirias.presentation.dashboard.dialog

import android.view.LayoutInflater
import com.mmfsin.quepreferirias.base.BaseDialog
import com.mmfsin.quepreferirias.databinding.DialogNoMoreBinding

class NoMoreDialog(val action: () -> Unit?) : BaseDialog<DialogNoMoreBinding>() {

    override fun inflateView(inflater: LayoutInflater) = DialogNoMoreBinding.inflate(inflater)

    override fun setListeners() {
        binding.btnAccept.setOnClickListener {
            action()
            dismiss()
        }
    }
}