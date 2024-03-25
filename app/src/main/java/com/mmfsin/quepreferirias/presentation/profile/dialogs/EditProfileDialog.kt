package com.mmfsin.quepreferirias.presentation.profile.dialogs

import android.app.Dialog
import android.view.LayoutInflater
import com.bumptech.glide.Glide
import com.mmfsin.quepreferirias.base.BaseDialog
import com.mmfsin.quepreferirias.databinding.DialogEditProfileBinding
import com.mmfsin.quepreferirias.domain.models.Session
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileDialog(val session: Session) : BaseDialog<DialogEditProfileBinding>() {

    override fun inflateView(inflater: LayoutInflater) = DialogEditProfileBinding.inflate(inflater)

    override fun setCustomViewDialog(dialog: Dialog) = bottomViewDialog(dialog)

    override fun setUI() {
        isCancelable = true
        binding.apply {
            activity?.let {
                Glide.with(it.applicationContext).load(session.imageUrl).into(ivImage.image)
            }
            tvName.text = session.name
        }
    }

    override fun setListeners() {
        binding.apply {
            ivClose.setOnClickListener { dismiss() }
            btnSave.setOnClickListener { }
        }
    }
}