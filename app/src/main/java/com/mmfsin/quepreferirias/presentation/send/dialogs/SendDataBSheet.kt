package com.mmfsin.quepreferirias.presentation.send.dialogs

import android.view.LayoutInflater
import com.mmfsin.quepreferirias.R
import com.mmfsin.quepreferirias.base.BaseBottomSheet
import com.mmfsin.quepreferirias.databinding.DialogSendDataBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SendDataBSheet(val navigate: (navGraph: Int) -> Unit) :
    BaseBottomSheet<DialogSendDataBinding>() {

    override fun inflateView(inflater: LayoutInflater) = DialogSendDataBinding.inflate(inflater)

    override fun setListeners() {
        binding.apply {
            sendDilemma.setOnClickListener { pressButton(R.navigation.nav_graph_send_dilemma) }
            sendDualism.setOnClickListener { pressButton(R.navigation.nav_graph_send_dualism) }
        }
    }

    private fun pressButton(navGraph: Int) {
        navigate(navGraph)
        dismiss()
    }
}