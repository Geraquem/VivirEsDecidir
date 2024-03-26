package com.mmfsin.quepreferirias.presentation.profile.dialogs

import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
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
            loading.visibility = View.INVISIBLE
            activity?.let {
                Glide.with(it.applicationContext).load(session.imageUrl).into(ivImage.image)
            }
            tvName.text = session.name
            etInstagram.setText(session.rrss?.instagram)
            etX.setText(session.rrss?.twitter)
            etTiktok.setText(session.rrss?.tiktok)
            etYoutube.setText(session.rrss?.youtube)
        }
    }

    override fun setListeners() {
        binding.apply {
            ivClose.setOnClickListener { dismiss() }
            btnSave.setOnClickListener {
                btnSave.visibility = View.INVISIBLE
                loading.visibility = View.VISIBLE
                etInstagram.disableET()
                etX.disableET()
                etTiktok.disableET()
                etYoutube.disableET()
                val instagram = etInstagram.text.toString().checkIfEmpty()
                val x = etX.text.toString().checkIfEmpty()
                val tiktok = etTiktok.text.toString().checkIfEmpty()
                val youtube = etYoutube.text.toString().checkIfEmpty()
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

    private fun EditText.disableET() {
        this.isEnabled = false
    }
}