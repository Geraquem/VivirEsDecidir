package com.mmfsin.quepreferirias.presentation.dashboard.conditionals.comments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mmfsin.quepreferirias.R
import com.mmfsin.quepreferirias.databinding.ItemCommentBinding
import com.mmfsin.quepreferirias.domain.models.Comment

class RecentCommentsAdapter(private val comments: List<Comment>) :
    RecyclerView.Adapter<RecentCommentsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemCommentBinding.bind(view)
        fun bind(comment: Comment) {
            binding.apply {
                tvComment.text = comment.name
                tvComment.text = comment.comment
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(comments[position])
    }

    override fun getItemCount(): Int = comments.size
}