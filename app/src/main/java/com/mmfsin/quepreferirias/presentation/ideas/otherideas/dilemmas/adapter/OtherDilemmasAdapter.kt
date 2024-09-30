package com.mmfsin.quepreferirias.presentation.ideas.otherideas.dilemmas.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.mmfsin.quepreferirias.R
import com.mmfsin.quepreferirias.databinding.ItemMyDilemmaBinding
import com.mmfsin.quepreferirias.domain.models.SendDilemma
import com.mmfsin.quepreferirias.presentation.ideas.otherideas.dilemmas.interfaces.IOtherDilemmasListener
import com.mmfsin.quepreferirias.utils.timestampToDate

class OtherDilemmasAdapter(
    private val data: List<SendDilemma>,
    private val listener: IOtherDilemmasListener
) : RecyclerView.Adapter<OtherDilemmasAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemMyDilemmaBinding.bind(view)
        private val c = binding.root.context
        fun bind(dilemma: SendDilemma) {
            binding.apply {
                tvTxtTop.text = dilemma.txtTop
                tvTxtBottom.text = dilemma.txtBottom
                val date = dilemma.timestamp.timestampToDate()
                tvDate.isVisible = false
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_my_dilemma, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dilemma = data[position]
        holder.bind(dilemma)
        holder.binding.clMain.setOnClickListener { listener.onDilemmaClick(dilemma.dilemmaId) }
    }

    override fun getItemCount(): Int = data.size
}