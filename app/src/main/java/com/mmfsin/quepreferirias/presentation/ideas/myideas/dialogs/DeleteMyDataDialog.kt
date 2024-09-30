package com.mmfsin.quepreferirias.presentation.ideas.myideas.dialogs

import android.app.Dialog
import android.view.LayoutInflater
import com.mmfsin.quepreferirias.R
import com.mmfsin.quepreferirias.base.BaseDialog
import com.mmfsin.quepreferirias.databinding.DialogDeleteBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeleteMyDataDialog(private val type: SentType, val delete: () -> Unit) :
    BaseDialog<DialogDeleteBinding>() {

    override fun inflateView(inflater: LayoutInflater) = DialogDeleteBinding.inflate(inflater)

    override fun setCustomViewDialog(dialog: Dialog) = centerViewDialog(dialog)

    override fun setUI() {
        isCancelable = true
        binding.apply {
            ivImage.setImageResource(R.drawable.ic_delete)
            val title = when (type) {
                SentType.DILEMMA -> R.string.delete_sent_dilemma_title
                SentType.DUALISM -> R.string.delete_sent_dualism_title
            }
            tvTitle.text = getString(title)
            btnAccept.text = getString(R.string.common_yes)
            btnCancel.text = getString(R.string.common_no)
        }
    }

    override fun setListeners() {
        binding.apply {
            btnAccept.setOnClickListener { delete() }
            btnCancel.setOnClickListener { dismiss() }
        }
    }
}

enum class SentType {
    DILEMMA,
    DUALISM
}