package com.mmfsin.quepreferirias.presentation.send.dialogs

import android.app.Dialog
import android.view.LayoutInflater
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
                tvSubtitle.text = getString(R.string.send_dualism_result_perfect_subtitle)
                btnResend.text = getString(R.string.send_dualism_result_resend)
            } else {
                ivImage.setImageResource(R.drawable.ic_error)
                tvTitle.text = getString(R.string.send_dualism_result_sww_title)
                tvSubtitle.text = getString(R.string.send_dualism_result_sww_subtitle)
                btnResend.text = getString(R.string.send_dualism_result_retry)
            }
            btnExit.text = getString(R.string.send_dualism_result_exit)
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