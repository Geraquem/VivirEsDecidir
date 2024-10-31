package com.mmfsin.quepreferirias.presentation.dashboard.common.dialog

import android.view.LayoutInflater
import androidx.core.view.isVisible
import com.mmfsin.quepreferirias.R
import com.mmfsin.quepreferirias.base.BaseBottomSheet
import com.mmfsin.quepreferirias.databinding.DialogReportBinding

class ReportDialog(val item: Int, val report: () -> Unit) : BaseBottomSheet<DialogReportBinding>() {

    override fun inflateView(inflater: LayoutInflater) = DialogReportBinding.inflate(inflater)

    override fun setUI() {
        isCancelable = true
        binding.apply {
            loading.isVisible = false
            val itemToReport = getString(item)
            tvText.text = getString(R.string.report_text, itemToReport)
        }
    }

    override fun setListeners() {
        binding.apply {
            btnReport.setOnClickListener {
                binding.apply {
                    loading.isVisible = true
                    btnReport.isEnabled = false
                    btnClose.isEnabled = false
                    report()
                }
            }
            btnClose.setOnClickListener { dismiss() }
        }
    }
}