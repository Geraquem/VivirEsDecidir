package com.mmfsin.quepreferirias.presentation.sendquestions.dialog

import android.view.LayoutInflater
import android.view.View
import com.mmfsin.quepreferirias.R
import com.mmfsin.quepreferirias.base.BaseDialog
import com.mmfsin.quepreferirias.databinding.DialogSqResultBinding

class SqResultDialog(val result: Boolean, val resultOk: () -> Unit?) :
    BaseDialog<DialogSqResultBinding>() {

    override fun inflateView(inflater: LayoutInflater) = DialogSqResultBinding.inflate(inflater)

    override fun setUI() {
        binding.apply {
            if (result) {
                lottieAnimation.setAnimation("check_animation.json")
                tvTitle.text = getString(R.string.sq_result_ok)
                btnError.visibility = View.GONE
            } else {
                lottieAnimation.setAnimation("error_animation.json")
                tvTitle.text = getString(R.string.sq_result_ko)
                btnAgain.visibility = View.GONE
            }
        }
    }

    override fun setListeners() {
        binding.apply {
            btnAgain.setOnClickListener {
                resultOk()
                dismiss()
            }

            btnError.setOnClickListener { dismiss() }
        }
    }
}