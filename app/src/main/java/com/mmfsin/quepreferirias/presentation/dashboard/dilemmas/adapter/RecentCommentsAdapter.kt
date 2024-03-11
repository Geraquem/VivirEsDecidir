package com.mmfsin.quepreferirias.presentation.dashboard.dilemmas.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mmfsin.quepreferirias.R
import com.mmfsin.quepreferirias.databinding.ItemRecentCommentBinding
import com.mmfsin.quepreferirias.domain.models.Comment

class RecentCommentsAdapter(
    private val comments: List<Comment>
) : RecyclerView.Adapter<RecentCommentsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemRecentCommentBinding.bind(view)
        val c = binding.root.context
        fun bind(comment: Comment) {
            binding.apply {
                Glide.with(binding.root.context).load(comment.image).into(image.image)
                tvName.text = comment.name
                tvComment.text = comment.comment
                tvDate.text = binding.root.context.getString(comment.since)
                val visible = if (comment.likes == 0L) View.GONE else View.VISIBLE
                tvLikes.visibility = visible
                tvLikes.text = setUpLikes(comment.likes)
            }
        }

        private fun setUpLikes(likes: Long): String {
            return when (likes) {
                0L -> ""
                1L, -1L -> c.getString(R.string.dashboard_comment_one_vote, likes.toString())
                else -> c.getString(R.string.dashboard_comment_votes, likes.toString())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_recent_comment, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(comments[position])
    }

    override fun getItemCount(): Int = comments.size
}