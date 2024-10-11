package com.mmfsin.quepreferirias.presentation.ideas.myideas.dualisms.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mmfsin.quepreferirias.R
import com.mmfsin.quepreferirias.databinding.ItemMyDilemmaBinding
import com.mmfsin.quepreferirias.domain.models.SendDualism
import com.mmfsin.quepreferirias.presentation.ideas.myideas.dualisms.interfaces.IMyDualismsListener
import com.mmfsin.quepreferirias.utils.timestampToDate

class MyDualismsAdapter(
    private val data: List<SendDualism>,
    private val listener: IMyDualismsListener
) : RecyclerView.Adapter<MyDualismsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemMyDilemmaBinding.bind(view)
        private val c = binding.root.context
        fun bind(dilemma: SendDualism) {
            binding.apply {
                tvTxtTop.text = dilemma.txtTop
                tvTxtBottom.text = dilemma.txtBottom
                val date = dilemma.timestamp.timestampToDate()
                tvDate.text = c.getString(R.string.my_data_item_date, date)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_my_dilemma, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dualism = data[position]
        holder.bind(dualism)
        holder.binding.clMain.setOnClickListener { listener.onMyDualismClick(dualism.dualismId) }
        holder.binding.clMain.setOnLongClickListener {
            listener.onMyDualismLongClick(dualism.dualismId)
            true
        }
    }

    override fun getItemCount(): Int = data.size
}