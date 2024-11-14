package com.mmfsin.quepreferirias.presentation.ideas.myideas.dilemmas.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.mmfsin.quepreferirias.R
import com.mmfsin.quepreferirias.databinding.ItemRvDilemmaBinding
import com.mmfsin.quepreferirias.domain.models.SendDilemma
import com.mmfsin.quepreferirias.presentation.ideas.myideas.dilemmas.interfaces.IMyDilemmasListener
import com.mmfsin.quepreferirias.utils.timestampToDate

class MyDilemmasAdapter(
    private val data: List<SendDilemma>,
    private val listener: IMyDilemmasListener
) : RecyclerView.Adapter<MyDilemmasAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemRvDilemmaBinding.bind(view)
        private val c = binding.root.context
        fun bind(dilemma: SendDilemma, separatorVisible: Boolean) {
            binding.apply {
                tvTxtTop.text = dilemma.txtTop
                tvTxtBottom.text = dilemma.txtBottom
                val date = dilemma.timestamp.timestampToDate()
                tvDate.text = c.getString(R.string.my_data_item_date, date)
                separator.isVisible = !separatorVisible
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_rv_dilemma, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dilemma = data[position]
        holder.bind(dilemma, position == data.size - 1)
        holder.binding.llMain.setOnClickListener { listener.onMyDilemmaClick(dilemma.dilemmaId) }
        holder.binding.llMain.setOnLongClickListener {
            listener.onMyDilemmaLongClick(dilemma.dilemmaId)
            true
        }
    }

    override fun getItemCount(): Int = data.size
}