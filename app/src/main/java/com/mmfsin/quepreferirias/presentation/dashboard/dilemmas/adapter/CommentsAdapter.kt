package com.mmfsin.quepreferirias.presentation.dashboard.dilemmas.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mmfsin.quepreferirias.R
import com.mmfsin.quepreferirias.databinding.ItemCommentBinding
import com.mmfsin.quepreferirias.domain.models.Comment
import com.mmfsin.quepreferirias.domain.models.CommentVote.*
import com.mmfsin.quepreferirias.presentation.dashboard.dilemmas.listener.ICommentsListener

class CommentsAdapter(
    private val listener: ICommentsListener,
    private val comments: List<Comment>
) : RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemCommentBinding.bind(view)
        fun bind(comment: Comment) {
            binding.apply {
                Glide.with(binding.root.context).load(comment.image).into(image.image)
                tvName.text = comment.name
                tvComment.text = comment.comment
                tvDate.text = binding.root.context.getString(comment.since)
                tvLikes.text = comment.likes.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comment = comments[position]
        holder.bind(comment)
        holder.binding.apply {
            ivVoteUp.setOnClickListener { listener.voteComment(VOTE_UP, comment) }
            ivVoteDown.setOnClickListener { listener.voteComment(VOTE_DOWN, comment) }
        }
    }

    override fun getItemCount(): Int = comments.size
}