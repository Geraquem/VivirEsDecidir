package com.mmfsin.quepreferirias.presentation.saved.dilemmas.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.mmfsin.quepreferirias.R
import com.mmfsin.quepreferirias.databinding.ItemRvDilemmaBinding
import com.mmfsin.quepreferirias.domain.models.DilemmaFav
import com.mmfsin.quepreferirias.presentation.saved.dilemmas.interfaces.IDilemmasFavListener

class DilemmasFavAdapter(
    private val favs: List<DilemmaFav>,
    private val listener: IDilemmasFavListener
) : RecyclerView.Adapter<DilemmasFavAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemRvDilemmaBinding.bind(view)
        fun bind(dilemma: DilemmaFav, separatorVisible: Boolean) {
            binding.apply {
                tvTxtTop.text = dilemma.txtTop
                tvTxtBottom.text = dilemma.txtBottom
                tvDate.isVisible = false
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
        val fav = favs[position]
        holder.bind(fav, position == favs.size - 1)
        holder.binding.llMain.setOnClickListener { listener.onDilemmaFavClick(fav.dilemmaId) }
        holder.binding.llMain.setOnLongClickListener {
            listener.onDilemmaFavLongClick(fav.dilemmaId)
            true
        }
    }

    override fun getItemCount(): Int = favs.size
}