package com.mmfsin.quepreferirias.presentation.profile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mmfsin.quepreferirias.R
import com.mmfsin.quepreferirias.databinding.ItemRrssBinding
import com.mmfsin.quepreferirias.domain.models.RRSSType
import com.mmfsin.quepreferirias.domain.models.getRRSSIcon

class RRSSAdapter(
    private val data: List<Pair<RRSSType, String>>,
) : RecyclerView.Adapter<RRSSAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemRrssBinding.bind(view)
        fun bind(data: Pair<RRSSType, String>) {
            binding.apply {
                ivImage.setImageResource(data.first.getRRSSIcon())
                tvRrss.text = data.second
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_rrss, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size
}