package com.mmfsin.quepreferirias.presentation.dashboard.comments.adapter

import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mmfsin.quepreferirias.R
import com.mmfsin.quepreferirias.databinding.ItemCommentReplyBinding
import com.mmfsin.quepreferirias.domain.models.CommentReply
import com.mmfsin.quepreferirias.presentation.dashboard.comments.interfaces.ICommentsRVListener

class RepliesAdapter(
    private val replies: List<CommentReply>,
    private val listener: ICommentsRVListener
) : RecyclerView.Adapter<RepliesAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemCommentReplyBinding.bind(view)
        private val c = binding.root.context
        fun bind(reply: CommentReply) {
            binding.apply {
                Glide.with(binding.root.context).load(reply.image).into(image.image)
                tvName.text = reply.userName
                tvComment.text = reply.reply
                tvLikes.text = reply.likes.toString()

                val up = ContextCompat.getColor(c, R.color.voted_up)
                val down = ContextCompat.getColor(c, R.color.voted_down)
                val neutro = ContextCompat.getColor(c, R.color.soft_black)
                ivVoteUp.setColorFilter(if (reply.votedUp) up else neutro, PorterDuff.Mode.SRC_IN)
                ivVoteDown.setColorFilter(
                    if (reply.votedDown) down else neutro,
                    PorterDuff.Mode.SRC_IN
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_comment_reply, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comment = replies[position]
        holder.bind(comment)
        holder.binding.apply {
            image.root.setOnClickListener { listener.onCommentNameClick(comment.userId) }
            tvName.setOnClickListener { listener.onCommentNameClick(comment.userId) }

//            ivCommentMenu.setOnClickListener {
//                listener.openCommentMenu(
//                    comment.commentId,
//                    comment.comment,
//                    comment.userId
//                )
//            }
//
//            ivVoteUp.setOnClickListener {
//                if (!comment.votedUp) vote(comment, CommentVote.VOTE_UP, position)
//            }
//            ivVoteDown.setOnClickListener {
//                if (!comment.votedDown) vote(comment, CommentVote.VOTE_DOWN, position)
//            }
        }
    }

    override fun getItemCount(): Int = replies.size
}