package com.mmfsin.quepreferirias.presentation.login.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mmfsin.quepreferirias.R
import com.mmfsin.quepreferirias.databinding.ItemLoginTextBinding

class LoginAdapter(private val data: List<String>) :
    RecyclerView.Adapter<LoginAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemLoginTextBinding.bind(view)
        fun bind(text: String) {
            binding.tvText.text = text
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_login_text, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size
}