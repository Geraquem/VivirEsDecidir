package com.mmfsin.quepreferirias.presentation.dashboard.common.dialog

import android.view.LayoutInflater
import com.mmfsin.quepreferirias.R
import com.mmfsin.quepreferirias.base.BaseBottomSheet
import com.mmfsin.quepreferirias.databinding.BsheetMenuDashboardBinding
import com.mmfsin.quepreferirias.presentation.dashboard.common.interfaces.IMenuDashboardListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MenuDashboardBSheet(
    private val isFav: Boolean?,
    private val listener: IMenuDashboardListener
) : BaseBottomSheet<BsheetMenuDashboardBinding>() {

    override fun inflateView(inflater: LayoutInflater) =
        BsheetMenuDashboardBinding.inflate(inflater)

    override fun setUI() {
        isFav?.let {
            val favText = if (it) R.string.menu_dashboard_delete_fav
            else R.string.menu_dashboard_set_fav
            binding.btnFavorite.text = getString(favText)
        }
    }

    override fun setListeners() {
        binding.apply {
            btnSendComment.setOnClickListener {
                listener.sendComment()
                dismiss()
            }

            btnFavorite.setOnClickListener {
                listener.setFavorite()
                dismiss()
            }
            btnCopyText.setOnClickListener {
                listener.copyText()
                dismiss()
            }
            btnShare.setOnClickListener {
                listener.share()
                dismiss()
            }
            btnReport.setOnClickListener {
                listener.report()
                dismiss()
            }
        }
    }
}