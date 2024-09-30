package com.mmfsin.quepreferirias.presentation.saved.dialogs

import android.app.Dialog
import android.view.LayoutInflater
import com.mmfsin.quepreferirias.R
import com.mmfsin.quepreferirias.base.BaseDialog
import com.mmfsin.quepreferirias.databinding.DialogDeleteBinding
import com.mmfsin.quepreferirias.utils.changeBgColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeleteFavDataDialog(val deleteFav: () -> Unit) : BaseDialog<DialogDeleteBinding>() {

    override fun inflateView(inflater: LayoutInflater) = DialogDeleteBinding.inflate(inflater)

    override fun setCustomViewDialog(dialog: Dialog) = centerViewDialog(dialog)

    override fun setUI() {
        isCancelable = true
        binding.apply {
            activity?.changeBgColor(rlTop.background, R.color.cancel)
            ivImage.setImageResource(R.drawable.ic_delete_fav)
            tvTitle.text = getString(R.string.delete_fav_data_title)
            btnAccept.text = getString(R.string.common_yes)
            btnCancel.text = getString(R.string.common_no)
        }
    }

    override fun setListeners() {
        binding.apply {
            btnAccept.setOnClickListener { deleteFav() }
            btnCancel.setOnClickListener { dismiss() }
        }
    }
}