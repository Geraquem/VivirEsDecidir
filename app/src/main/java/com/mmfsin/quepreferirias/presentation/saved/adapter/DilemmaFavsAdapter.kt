package com.mmfsin.quepreferirias.presentation.saved.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mmfsin.quepreferirias.R
import com.mmfsin.quepreferirias.databinding.ItemDilemmaFavBinding
import com.mmfsin.quepreferirias.domain.models.DilemmaFav
import com.mmfsin.quepreferirias.presentation.saved.interfaces.IDilemmaFavListener

class DilemmaFavsAdapter(
    private val favs: List<DilemmaFav>,
    private val listener: IDilemmaFavListener
) : RecyclerView.Adapter<DilemmaFavsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemDilemmaFavBinding.bind(view)
        fun bind(dilemma: DilemmaFav) {
            binding.apply {
                tvTxtTop.text = dilemma.txtTop
                tvTxtBottom.text = dilemma.txtBottom
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_dilemma_fav, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fav = favs[position]
        holder.bind(fav)
        holder.binding.clMain.setOnClickListener { listener.onDilemmaFavClick(fav.dilemmaId) }
    }

    override fun getItemCount(): Int = favs.size
}