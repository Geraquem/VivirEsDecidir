package com.mmfsin.quepreferirias.presentation.send.dialogs

import android.app.Dialog
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import com.mmfsin.quepreferirias.R
import com.mmfsin.quepreferirias.base.BaseDialog
import com.mmfsin.quepreferirias.databinding.DialogSendDataResultBinding
import com.mmfsin.quepreferirias.presentation.send.interfaces.ISendDataResultListener
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SendDataResultDialog(
    private val wasOk: Boolean,
    private val listener: ISendDataResultListener
) :
    BaseDialog<DialogSendDataResultBinding>() {

    override fun inflateView(inflater: LayoutInflater) =
        DialogSendDataResultBinding.inflate(inflater)

    override fun setCustomViewDialog(dialog: Dialog) = centerViewDialog(dialog)

    override fun setUI() {
        isCancelable = false
        binding.apply {
            if (wasOk) {
                ivImage.setImageResource(R.drawable.ic_success)
                tvTitle.text = getString(R.string.send_dualism_result_perfect_title)
                btnResend.text = getString(R.string.send_dualism_result_resend)
                changeBgColor(rlTop.background, R.color.success)
                changeBgColor(btnResend.background, R.color.white)
                changeBgColor(btnExit.background, R.color.white)

            } else {
                ivImage.setImageResource(R.drawable.ic_error)
                tvTitle.text = getString(R.string.send_dualism_result_sww_title)
                btnResend.text = getString(R.string.send_dualism_result_retry)
                changeBgColor(rlTop.background, R.color.cancel)
                changeBgColor(btnResend.background, R.color.white)
                changeBgColor(btnExit.background, R.color.cancel)
            }
            btnExit.text = getString(R.string.send_dualism_result_exit)
        }
    }

    private fun changeBgColor(background: Drawable, color: Int) {
        context?.let {
            if (background is GradientDrawable) {
                background.setColor(ContextCompat.getColor(it, color))
            }
        }
    }

    override fun setListeners() {
        binding.apply {
            btnResend.setOnClickListener {
                if (wasOk) listener.sendAnother()
                dismiss()
            }
            btnExit.setOnClickListener {
                listener.close()
                dismiss()
            }
        }
    }
}