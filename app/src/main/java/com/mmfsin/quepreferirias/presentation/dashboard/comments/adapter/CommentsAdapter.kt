package com.mmfsin.quepreferirias.presentation.dashboard.comments.adapter

import android.annotation.SuppressLint
import android.graphics.PorterDuff.Mode.SRC_IN
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mmfsin.quepreferirias.R
import com.mmfsin.quepreferirias.databinding.ItemCommentBinding
import com.mmfsin.quepreferirias.domain.models.Comment
import com.mmfsin.quepreferirias.domain.models.CommentReply
import com.mmfsin.quepreferirias.domain.models.CommentVote
import com.mmfsin.quepreferirias.domain.models.CommentVote.VOTE_DOWN
import com.mmfsin.quepreferirias.domain.models.CommentVote.VOTE_UP
import com.mmfsin.quepreferirias.presentation.dashboard.comments.interfaces.ICommentsRVListener
import com.mmfsin.quepreferirias.utils.getRepliesText

class CommentsAdapter(
    private val comments: MutableList<Comment>,
    private val listener: ICommentsRVListener
) : RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemCommentBinding.bind(view)
        private val c = binding.root.context
        fun bind(comment: Comment, listener: ICommentsRVListener) {
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

                if (comment.replies.isNotEmpty()) {
                    rvReplies.apply {
                        layoutManager = LinearLayoutManager(context)
                        adapter = RepliesAdapter(comment.replies, listener)
                        tvSeeReplies.text = c.getRepliesText(comment.replies.size)
                    }
                }
                if (comment.repliesOpened) {
                    tvSeeReplies.isVisible = false
                    rvReplies.isVisible = true
                } else {
                    tvSeeReplies.isVisible = true
                    rvReplies.isVisible = false
                }
            }
        }
    }

    fun updateCommentVotes(vote: CommentVote, position: Int, alreadyVoted: Boolean) {
        val comment = comments[position]
        var likes = comment.likes
        val count = if (alreadyVoted) 2 else 1
        when (vote) {
            VOTE_UP -> {
                likes = likes.plus(count)
                comment.votedUp = true
                comment.votedDown = false
            }

            VOTE_DOWN -> {
                likes = likes.minus(count)
                comment.votedUp = false
                comment.votedDown = true
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
        holder.bind(comment, listener)
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

            ivVoteUp.setOnClickListener {
                if (!comment.votedUp) vote(comment, VOTE_UP, position)
            }
            ivVoteDown.setOnClickListener {
                if (!comment.votedDown) vote(comment, VOTE_DOWN, position)
            }

            tvSeeReplies.setOnClickListener {
                comment.repliesOpened = true
                notifyItemChanged(position)
            }
        }
    }

    fun addComments(newComments: List<Comment>) {
        val startPosition = comments.size
        comments.addAll(newComments)
        notifyItemRangeInserted(startPosition, newComments.size)
    }

    fun getComment(commentId: String): Comment? = comments.firstOrNull { it.commentId == commentId }

    fun replyComment(reply: CommentReply) {
        val commentPosition = comments.indexOfFirst { it.commentId == reply.commentId }
        if (commentPosition != -1) {
            try {
                val comment = comments[commentPosition]
                comment.replies.add(reply)
                comment.repliesOpened = true
                notifyItemChanged(commentPosition)
            } catch (e: Exception) {
                Log.e("error", "error getting comment")
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun deleteComment(commentId: String) {
        val position = comments.indexOfFirst { it.commentId == commentId }
        if (position != -1) {
            comments.removeAt(position)
            if (comments.isEmpty()) notifyDataSetChanged()
            else notifyItemRemoved(position)
        }
    }

    fun deleteCommentReply(commentId: String, replyId: String) {
        val commentIndex = comments.indexOfFirst { it.commentId == commentId }
        if (commentIndex != -1) {
            val comment = getComment(commentId)

            val replies = comment?.replies
            replies?.let {
                val iterator = comment.replies.iterator()
                while (iterator.hasNext()) {
                    val reply = iterator.next()
                    if (reply.replyId == replyId) {
                        iterator.remove()
                    }
                }
                val updatedComment = comment.copy(replies = it.toMutableList())
                comments[commentIndex] = updatedComment
                notifyItemChanged(commentIndex)
            }
        }
    }

    private fun vote(comment: Comment, vote: CommentVote, position: Int) {
        listener.voteComment(comment.commentId, vote, comment.likes, position)
    }

    override fun getItemCount(): Int = comments.size
}