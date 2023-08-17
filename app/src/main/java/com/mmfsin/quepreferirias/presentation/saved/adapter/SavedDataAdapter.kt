package com.mmfsin.quepreferirias.presentation.saved.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mmfsin.quepreferirias.R
import com.mmfsin.quepreferirias.databinding.ItemDataSavedBinding
import com.mmfsin.quepreferirias.domain.models.SavedData
import com.mmfsin.quepreferirias.presentation.saved.interfaces.ISavedDataListener

class SavedDataAdapter(
    private val savedDataList: List<SavedData>, private val listener: ISavedDataListener
) : RecyclerView.Adapter<SavedDataAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemDataSavedBinding.bind(view)
        fun bind(data: SavedData) {
            binding.apply {
                tvTxtTop.text = data.txtTop
                tvTxtBottom.text = data.txtBottom
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_data_saved, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(savedDataList[position])
        holder.itemView.setOnClickListener { listener.onDataClicked(savedDataList[position].dataId) }
    }

    override fun getItemCount(): Int = savedDataList.size
}