package com.mmfsin.quepreferirias.presentation.dashboard.comments.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mmfsin.quepreferirias.R
import com.mmfsin.quepreferirias.databinding.ItemCommentBinding
import com.mmfsin.quepreferirias.domain.models.Comment
import com.mmfsin.quepreferirias.domain.models.CommentReply
import com.mmfsin.quepreferirias.presentation.dashboard.comments.interfaces.ICommentsRVListener
import com.mmfsin.quepreferirias.utils.getRepliesText

class SentCommentsAdapter(
    private val comments: MutableList<Comment>,
    private val listener: ICommentsRVListener
) : RecyclerView.Adapter<SentCommentsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemCommentBinding.bind(view)
        val c: Context = binding.root.context
        fun bind(comment: Comment) {
            binding.apply {
                Glide.with(binding.root.context).load(comment.image).into(image.image)
                tvName.text = comment.name
                tvComment.text = comment.comment
                tvDate.text = binding.root.context.getString(R.string.comments_right_now)
                llVotes.isVisible = false

                tvSeeReplies.isVisible = false
                rvReplies.isVisible = false

                if (comment.replies.isNotEmpty()) {
                    val repliesAdapter = RepliesAdapter(comment.replies)
                    rvReplies.apply {
                        layoutManager = LinearLayoutManager(context)
                        adapter = repliesAdapter
                        tvSeeReplies.isVisible = true
                        tvSeeReplies.text = c.getRepliesText(comment.replies.size)
                    }
                }
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
                    comment.comment,
                    comment.userId
                )
            }
            tvSeeReplies.setOnClickListener {
                tvSeeReplies.isVisible = false
                rvReplies.isVisible = true
            }
        }
    }

    fun addComments(newComments: List<Comment>) {
        comments.addAll(0, newComments)
        notifyItemRangeInserted(0, newComments.size)

    }

    fun getComment(commentId: String): Comment? = comments.firstOrNull { it.commentId == commentId }

    fun replyComment(commentId: String, reply: CommentReply) {
        val commentPosition = comments.indexOfFirst { it.commentId == commentId }
        if (commentPosition != -1) {
            try {
                val comment = comments[commentPosition]
                comment.replies.add(reply)
                notifyItemChanged(commentPosition)
            } catch (e: Exception) {
                Log.e("error", "error getting comment")
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearData() {
        comments.clear()
        notifyItemRangeChanged(0, comments.size)
    }

    fun deleteComment(commentId: String) {
        val position = comments.indexOfFirst { it.commentId == commentId }
        if (position != -1) {
            comments.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    override fun getItemCount(): Int = comments.size
}