package com.mmfsin.quepreferirias.presentation.single.dialogs

import android.app.Dialog
import android.view.LayoutInflater
import com.mmfsin.quepreferirias.R
import com.mmfsin.quepreferirias.base.BaseDialog
import com.mmfsin.quepreferirias.databinding.DialogErrorFavDataBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ErrorDataDialog(val action: () -> Unit) : BaseDialog<DialogErrorFavDataBinding>() {

    override fun inflateView(inflater: LayoutInflater) = DialogErrorFavDataBinding.inflate(inflater)

    override fun setCustomViewDialog(dialog: Dialog) = centerViewDialog(dialog)

    override fun setUI() {
        isCancelable = false
        binding.apply {
            tvDescriptionTwo.text = getString(R.string.saved_error_description_two)
        }
    }

    override fun setListeners() {
        binding.btnBack.setOnClickListener {
            dismiss()
            action()
        }
    }
}