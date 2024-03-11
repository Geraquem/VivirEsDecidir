package com.mmfsin.quepreferirias.presentation.dashboard.dilemmas.adapter

import android.graphics.PorterDuff.Mode
import android.graphics.PorterDuff.Mode.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mmfsin.quepreferirias.R
import com.mmfsin.quepreferirias.databinding.ItemCommentBinding
import com.mmfsin.quepreferirias.domain.models.Comment
import com.mmfsin.quepreferirias.domain.models.CommentVote
import com.mmfsin.quepreferirias.domain.models.CommentVote.VOTE_DOWN
import com.mmfsin.quepreferirias.domain.models.CommentVote.VOTE_UP
import com.mmfsin.quepreferirias.presentation.dashboard.dilemmas.listener.ICommentsListener

class CommentsAdapter(
    private val listener: ICommentsListener,
    private val comments: List<Comment>
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

                val up = getColor(c, R.color.voted_up)
                val down = getColor(c, R.color.voted_down)
                val neutro = getColor(c, R.color.soft_black)
                ivVoteUp.setColorFilter(if (comment.votedUp) up else neutro, SRC_IN)
                ivVoteDown.setColorFilter(if (comment.votedDown) down else neutro, SRC_IN)
            }
        }
    }

    fun updateCommentVoted(vote: CommentVote, position: Int) {
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
            ivVoteUp.setOnClickListener {
                if (!comment.votedUp) vote(comment, VOTE_UP, position)
            }
            ivVoteDown.setOnClickListener {
                if (!comment.votedDown) vote(comment, VOTE_DOWN, position)
            }
        }
    }

    private fun vote(comment: Comment, vote: CommentVote, position: Int) {
        listener.voteComment(comment.commentId, vote, comment.likes, position)
    }

    override fun getItemCount(): Int = comments.size
}