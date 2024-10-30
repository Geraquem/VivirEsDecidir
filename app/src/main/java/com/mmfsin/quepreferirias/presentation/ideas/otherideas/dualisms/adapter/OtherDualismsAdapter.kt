package com.mmfsin.quepreferirias.presentation.ideas.otherideas.dualisms.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.mmfsin.quepreferirias.R
import com.mmfsin.quepreferirias.databinding.ItemRvDualismBinding
import com.mmfsin.quepreferirias.domain.models.SendDualism
import com.mmfsin.quepreferirias.presentation.ideas.otherideas.dualisms.interfaces.IOtherDualismsListener

class OtherDualismsAdapter(
    private val data: List<SendDualism>,
    private val listener: IOtherDualismsListener
) : RecyclerView.Adapter<OtherDualismsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemRvDualismBinding.bind(view)
        private val c = binding.root.context
        fun bind(dualism: SendDualism) {
            binding.apply {
                tvTxtTop.text = dualism.txtTop
                tvTxtBottom.text = dualism.txtBottom
                tvDate.isVisible = false
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_rv_dualism, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dualism = data[position]
        holder.bind(dualism)
        holder.binding.clMain.setOnClickListener { listener.onDualismClick(dualism.dualismId) }
    }

    override fun getItemCount(): Int = data.size
}