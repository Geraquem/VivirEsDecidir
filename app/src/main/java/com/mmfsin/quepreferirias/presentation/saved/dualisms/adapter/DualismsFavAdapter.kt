package com.mmfsin.quepreferirias.presentation.saved.dualisms.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mmfsin.quepreferirias.R
import com.mmfsin.quepreferirias.databinding.ItemRvDualismBinding
import com.mmfsin.quepreferirias.domain.models.DualismFav
import com.mmfsin.quepreferirias.presentation.saved.dualisms.interfaces.IDualismsFavListener

class DualismsFavAdapter(
    private val favs: List<DualismFav>,
    private val listener: IDualismsFavListener
) : RecyclerView.Adapter<DualismsFavAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemRvDualismBinding.bind(view)
        fun bind(dilemma: DualismFav) {
            binding.apply {
                tvTxtTop.text = dilemma.txtTop
                tvTxtBottom.text = dilemma.txtBottom
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_rv_dualism, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fav = favs[position]
        holder.bind(fav)
        holder.binding.clMain.setOnClickListener { listener.onDualismFavClick(fav.dualismId) }
        holder.binding.clMain.setOnLongClickListener {
            listener.onDualismFavLongClick(fav.dualismId)
            true
        }
    }

    override fun getItemCount(): Int = favs.size
}