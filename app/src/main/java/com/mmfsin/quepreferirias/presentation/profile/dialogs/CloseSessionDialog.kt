package com.mmfsin.quepreferirias.presentation.profile.dialogs

import android.app.Dialog
import android.view.LayoutInflater
import com.mmfsin.quepreferirias.R
import com.mmfsin.quepreferirias.base.BaseDialog
import com.mmfsin.quepreferirias.databinding.DialogDeleteBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CloseSessionDialog(val exit: () -> Unit) : BaseDialog<DialogDeleteBinding>() {

    override fun inflateView(inflater: LayoutInflater) = DialogDeleteBinding.inflate(inflater)

    override fun setCustomViewDialog(dialog: Dialog) = centerViewDialog(dialog)

    override fun setUI() {
        isCancelable = true
        binding.apply {
            ivImage.setImageResource(R.drawable.ic_exit)
            tvTitle.text = getString(R.string.profile_close_session_confirm)
            btnAccept.text = getString(R.string.common_yes)
            btnCancel.text = getString(R.string.common_no)
        }
    }

    override fun setListeners() {
        binding.apply {
            btnAccept.setOnClickListener { exit() }
            btnCancel.setOnClickListener { dismiss() }
        }
    }
}