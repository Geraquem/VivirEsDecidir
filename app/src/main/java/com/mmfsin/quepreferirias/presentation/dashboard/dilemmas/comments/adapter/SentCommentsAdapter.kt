package com.mmfsin.quepreferirias.presentation.dashboard.dilemmas.comments.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mmfsin.quepreferirias.R
import com.mmfsin.quepreferirias.databinding.ItemCommentBinding
import com.mmfsin.quepreferirias.domain.models.Comment
import com.mmfsin.quepreferirias.domain.models.CommentVote
import com.mmfsin.quepreferirias.presentation.dashboard.dilemmas.interfaces.ICommentsRVListener

class SentCommentsAdapter(
    private val comments: MutableList<Comment>,
    private val listener: ICommentsRVListener
) : RecyclerView.Adapter<SentCommentsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemCommentBinding.bind(view)
        private val c = binding.root.context
        fun bind(comment: Comment) {
            binding.apply {
                Glide.with(binding.root.context).load(comment.image).into(image.image)
                tvName.text = comment.name
                tvComment.text = comment.comment
                tvDate.text = binding.root.context.getString(R.string.comments_right_now)
                llVotes.isVisible = false
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
            image.root.setOnClickListener { listener.onCommentNameClick(comment.userId) }
            tvName.setOnClickListener { listener.onCommentNameClick(comment.userId) }
            ivCommentMenu.setOnClickListener {
                listener.openCommentMenu(
                    comment.commentId,
                    comment.userId
                )
            }
        }
    }

    fun addComments(newComments: List<Comment>) {
//        comments.addAll(newComments)
//        notifyItemRangeInserted(comments.size - newComments.size, newComments.size)
        comments.addAll(0, newComments)
        notifyItemRangeInserted(0, newComments.size)

    }

    fun getComment(commentId: String): Comment? = comments.firstOrNull { it.commentId == commentId }

    @SuppressLint("NotifyDataSetChanged")
    fun clearData() {
        comments.clear()
        notifyDataSetChanged()
    }

    fun deleteComment(commentId: String) {
        val position = comments.indexOfFirst { it.commentId == commentId }
        if (position != -1) {
            comments.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, comments.size)
        }
    }

    private fun vote(comment: Comment, vote: CommentVote, position: Int) {
        listener.voteComment(comment.commentId, vote, comment.likes, position)
    }

    override fun getItemCount(): Int = comments.size
}