package com.mmfsin.quepreferirias.base.dialog

import android.view.LayoutInflater
import com.mmfsin.quepreferirias.base.BaseDialog
import com.mmfsin.quepreferirias.databinding.DialogErrorBinding

class ErrorDialog : BaseDialog<DialogErrorBinding>() {

    override fun inflateView(inflater: LayoutInflater) = DialogErrorBinding.inflate(inflater)

    override fun setListeners() {
        binding.btnAccept.setOnClickListener {
            activity?.onBackPressed()
            dismiss()
        }
    }
}