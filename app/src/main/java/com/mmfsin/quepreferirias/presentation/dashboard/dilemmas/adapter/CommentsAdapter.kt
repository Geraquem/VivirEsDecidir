package com.mmfsin.quepreferirias.presentation.dashboard.dilemmas.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mmfsin.quepreferirias.R
import com.mmfsin.quepreferirias.databinding.ItemCommentBinding
import com.mmfsin.quepreferirias.domain.models.Comment
import com.mmfsin.quepreferirias.domain.models.CommentVote
import com.mmfsin.quepreferirias.domain.models.CommentVote.UNVOTE
import com.mmfsin.quepreferirias.domain.models.CommentVote.VOTE
import com.mmfsin.quepreferirias.presentation.dashboard.dilemmas.listener.ICommentsListener

class CommentsAdapter(
    private val comments: List<Comment>,
    private val listener: ICommentsListener
) : RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemCommentBinding.bind(view)
        private val c = binding.root.context
        fun bind(comment: Comment) {
            binding.apply {
                Glide.with(binding.root.context).load(comment.image).into(image.image)
                tvName.text = comment.name
                tvComment.text = comment.comment
                tvDate.text = binding.root.context.getString(comment.since)
                tvLikes.text = comment.likes.toString()

                val icon = if (comment.voted) R.drawable.ic_heart_on else R.drawable.ic_heart_off
                ivLike.setImageResource(icon)
            }
        }
    }

    fun updateCommentVotes(vote: CommentVote, position: Int) {
        val comment = comments[position]
        var likes = comment.likes
        when (vote) {
            VOTE -> {
                likes = likes.plus(1)
                comment.voted = true
            }

            UNVOTE -> {
                likes = likes.minus(1)
                comment.voted = false
            }
        }
        comment.likes = likes
        notifyItemChanged(position)
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
            image.root.setOnClickListener { listener.onCommentNameClick(comment.userId) }
            tvName.setOnClickListener { listener.onCommentNameClick(comment.userId) }
            ivLike.setOnClickListener {
                if (comment.voted) vote(comment, UNVOTE, position)
                else vote(comment, VOTE, position)
            }
        }
    }

    private fun vote(comment: Comment, vote: CommentVote, position: Int) {
        listener.voteComment(comment.commentId, vote, comment.likes, position)
    }

    override fun getItemCount(): Int = comments.size
}