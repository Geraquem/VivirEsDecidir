package com.mmfsin.quepreferirias.presentation.profile.dialogs

import android.app.Dialog
import android.view.LayoutInflater
import com.bumptech.glide.Glide
import com.mmfsin.quepreferirias.base.BaseDialog
import com.mmfsin.quepreferirias.databinding.DialogEditProfileBinding
import com.mmfsin.quepreferirias.domain.models.RRSS
import com.mmfsin.quepreferirias.domain.models.Session
import com.mmfsin.quepreferirias.presentation.profile.listeners.IRRSSListener
import com.mmfsin.quepreferirias.utils.checkIfEmpty
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RRSSDialog(val session: Session, val listener: IRRSSListener) :
    BaseDialog<DialogEditProfileBinding>() {

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
            btnSave.setOnClickListener {
                val instagram = etInstagram.text.toString().checkIfEmpty()
                val x = etInstagram.text.toString().checkIfEmpty()
                val tiktok = etInstagram.text.toString().checkIfEmpty()
                val youtube = etInstagram.text.toString().checkIfEmpty()

                /** si no se ha cambiado ninguna no llamar a server */
                if (instagram != null && x != null && tiktok != null && youtube != null) {
                    listener.updateRRSS(
                        RRSS(
                            instagram = instagram,
                            twitter = x,
                            tiktok = tiktok,
                            youtube = youtube
                        )
                    )
                }
            }
        }
    }
}