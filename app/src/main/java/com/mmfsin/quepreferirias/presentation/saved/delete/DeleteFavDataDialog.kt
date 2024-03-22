package com.mmfsin.quepreferirias.presentation.saved.delete

import android.app.Dialog
import android.view.LayoutInflater
import com.mmfsin.quepreferirias.R
import com.mmfsin.quepreferirias.base.BaseDialog
import com.mmfsin.quepreferirias.databinding.DialogDeleteBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeleteFavDataDialog(val deleteFav: () -> Unit) : BaseDialog<DialogDeleteBinding>() {

    override fun inflateView(inflater: LayoutInflater) = DialogDeleteBinding.inflate(inflater)

    override fun setCustomViewDialog(dialog: Dialog) = centerViewDialog(dialog)

    override fun setUI() {
        isCancelable = true
        binding.apply {
            ivImage.setImageResource(R.drawable.ic_delete_fav)
            tvTitle.text = getString(R.string.delete_fav_data_title)
            btnAccept.text = getString(R.string.delete_fav_data_accept)
            btnCancel.text = getString(R.string.delete_fav_data_reject)
        }
    }

    override fun setListeners() {
        binding.apply {
            btnAccept.setOnClickListener { deleteFav() }
            btnCancel.setOnClickListener { dismiss() }
        }
    }
}