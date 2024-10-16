package com.mmfsin.quepreferirias.presentation.send.dialogs

import android.app.Dialog
import android.view.LayoutInflater
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.target.ImageViewTarget
import com.mmfsin.quepreferirias.R
import com.mmfsin.quepreferirias.base.BaseDialog
import com.mmfsin.quepreferirias.databinding.DialogSendDataResultBinding
import com.mmfsin.quepreferirias.presentation.send.interfaces.ISendDataResultListener
import com.mmfsin.quepreferirias.utils.changeBgColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SendDataResultDialog(
    private val wasOk: Boolean,
    private val listener: ISendDataResultListener
) : BaseDialog<DialogSendDataResultBinding>() {

    override fun inflateView(inflater: LayoutInflater) =
        DialogSendDataResultBinding.inflate(inflater)

    override fun setCustomViewDialog(dialog: Dialog) = centerViewDialog(dialog)

    override fun setUI() {
        isCancelable = false
        binding.apply {
            if (wasOk) {
                ivImage.setImageResource(R.drawable.ic_success)
                setGif()
                tvTitle.text = getString(R.string.send_dualism_result_perfect_title)
                btnResend.text = getString(R.string.send_dualism_result_resend)
                activity?.changeBgColor(rlTop.background, R.color.main_color)

            } else {
                ivImage.setImageResource(R.drawable.ic_error)
                ivGif.isVisible = true
                tvTitle.text = getString(R.string.send_dualism_result_sww_title)
                tvSubtitle.isVisible = false
                tvSubtitleTwo.isVisible = false
                btnResend.text = getString(R.string.send_dualism_result_retry)
                activity?.changeBgColor(rlTop.background, R.color.cancel)

            }
            btnExit.text = getString(R.string.send_dualism_result_exit)
        }
    }

    private fun setGif() {
        Glide.with(this)
            .asGif()
            .load(R.drawable.paper_plane)
            .into(object : ImageViewTarget<GifDrawable>(binding.ivGif) {
                override fun setResource(resource: GifDrawable?) {
                    resource?.let {
                        it.setLoopCount(1)
                        binding.ivGif.setImageDrawable(it)
                        it.start()
                    }
                }
            })
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