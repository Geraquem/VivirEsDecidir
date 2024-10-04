package com.mmfsin.quepreferirias.presentation.dashboard.common.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mmfsin.quepreferirias.R
import com.mmfsin.quepreferirias.databinding.DialogMenuDashboardBinding
import com.mmfsin.quepreferirias.presentation.dashboard.common.interfaces.IMenuDashboardListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MenuDashboardDialog(
    private val isFav: Boolean?,
    private val listener: IMenuDashboardListener
) : BottomSheetDialogFragment() {

    private var _binding: DialogMenuDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogMenuDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUI()
        setListeners()
    }

    private fun setUI() {
        isFav?.let {
            val favText = if (it) R.string.menu_dashboard_delete_fav
            else R.string.menu_dashboard_set_fav
            binding.btnFavorite.text = getString(favText)
        }
    }

    private fun setListeners() {
        binding.apply {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}