package com.mmfsin.quepreferirias.presentation.dashboard.comments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mmfsin.quepreferirias.R
import com.mmfsin.quepreferirias.base.BaseFragment
import com.mmfsin.quepreferirias.databinding.FragmentCommentsBinding
import com.mmfsin.quepreferirias.domain.models.Comment
import com.mmfsin.quepreferirias.domain.models.CommentReply
import com.mmfsin.quepreferirias.domain.models.CommentVote
import com.mmfsin.quepreferirias.domain.models.DataToRespondComment
import com.mmfsin.quepreferirias.domain.models.Session
import com.mmfsin.quepreferirias.presentation.dashboard.comments.adapter.CommentsAdapter
import com.mmfsin.quepreferirias.presentation.dashboard.comments.adapter.SentCommentsAdapter
import com.mmfsin.quepreferirias.presentation.dashboard.comments.dialogs.menu.MenuCommentBSheet
import com.mmfsin.quepreferirias.presentation.dashboard.comments.dialogs.respond.RespondCommentBSheet
import com.mmfsin.quepreferirias.presentation.dashboard.comments.interfaces.ICommentMenuListener
import com.mmfsin.quepreferirias.presentation.dashboard.comments.interfaces.ICommentsListener
import com.mmfsin.quepreferirias.presentation.dashboard.comments.interfaces.ICommentsRVListener
import com.mmfsin.quepreferirias.presentation.dashboard.common.dialog.ReportDialog
import com.mmfsin.quepreferirias.presentation.models.DashboardType
import com.mmfsin.quepreferirias.presentation.single.dialogs.ErrorDataDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommentsFragment(
    private val dataId: String,
    private val type: DashboardType,
    private val listener: ICommentsListener
) : BaseFragment<FragmentCommentsBinding, CommentsViewModel>(), ICommentsRVListener,
    ICommentMenuListener {

    override val viewModel: CommentsViewModel by viewModels()
    private lateinit var mContext: Context

    private var hasSession = false
    private var commentsAdapter: CommentsAdapter? = null
    private var sentCommentsAdapter: SentCommentsAdapter? = null

    private var thereAreMoreComments: Boolean = true

    private var reportDialog: ReportDialog? = null

    override fun inflateView(
        inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentCommentsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.checkSessionInitiated()
    }

    override fun setUI() {
        binding.apply {
            tvNoComments.isVisible = false
            loadingComments.isVisible = false
            loadingMore.isVisible = false
        }
    }

    override fun observe() {
        viewModel.event.observe(this) { event ->
            when (event) {
                is CommentsEvent.CheckIfSession -> {
                    hasSession = event.hasSession
                    viewModel.getComments(dataId, type)
                }

                is CommentsEvent.Comments -> setUpComments(event.comments)
                is CommentsEvent.CommentAlreadyVoted -> {
                    Toast.makeText(
                        activity?.applicationContext,
                        getString(R.string.comment_already_voted),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is CommentsEvent.CommentVotedResult -> {
                    commentsAdapter?.updateCommentVotes(
                        event.vote,
                        event.position,
                        event.alreadyVoted
                    )
                }

                is CommentsEvent.GetSessionToRespondComment -> {
                    openRespondCommentSheet(event.session, event.data)
                }

                is CommentsEvent.CommentReported -> commentReported()

                is CommentsEvent.SWW -> error()
            }
        }
    }

    private fun setUpComments(comments: List<Comment>) {
        binding.apply {
            if (comments.isEmpty()) thereAreMoreComments = false
            loadingMore.isVisible = false
            loadingComments.isVisible = false
            if (rvComments.adapter == null) {
                tvNoComments.isVisible = comments.isEmpty()
                rvComments.apply {
                    layoutManager = LinearLayoutManager(mContext)
                    commentsAdapter = CommentsAdapter(
                        comments.toMutableList(),
                        this@CommentsFragment
                    )
                    adapter = commentsAdapter
                }
            } else commentsAdapter?.addComments(comments)
        }
    }

    override fun onCommentNameClick(userId: String) = listener.navigateToUserProfile(userId)

    override fun onReplyNameClick(userId: String) = listener.navigateToUserProfile(userId)

    override fun openCommentMenu(commentId: String, commentText: String, userId: String) {
        val dialog = MenuCommentBSheet(
            dataId,
            type,
            commentId,
            commentText,
            userId,
            this@CommentsFragment
        )
        activity?.let { dialog.show(it.supportFragmentManager, "") }
    }

    override fun respondComment(dataToRespondComment: DataToRespondComment) =
        viewModel.getSessionToRespondComment(dataToRespondComment)

    private fun openRespondCommentSheet(
        session: Session?,
        data: DataToRespondComment
    ) {
        session?.let { userData ->
            val dialog = RespondCommentBSheet(data, userData, this@CommentsFragment)
            activity?.let { dialog.show(it.supportFragmentManager, "") }
        } ?: run { listener.shouldInitiateSession() }
    }

    override fun respondComment(commentId: String, reply: CommentReply) {
        sentCommentsAdapter?.replyComment(commentId, reply)
        commentsAdapter?.replyComment(commentId, reply)
    }

    override fun commentDeleted(commentId: String) {
        sentCommentsAdapter?.deleteComment(commentId)
        commentsAdapter?.deleteComment(commentId)
    }

    override fun voteComment(
        commentId: String,
        vote: CommentVote,
        likes: Long,
        position: Int
    ) {
        if (hasSession) viewModel.voteComment(dataId, type, commentId, vote, likes, position)
        else listener.shouldInitiateSession()
    }

    fun updateComments() {
        if (thereAreMoreComments) {
            binding.loadingMore.isVisible = true
            viewModel.getComments(dataId, type)
        } else {
            binding.loadingMore.isVisible = false
        }
    }

    fun commentSent(comment: Comment) {
        val list = mutableListOf(comment)
        binding.apply {
            if (rvSentComments.adapter == null) {
                rvSentComments.apply {
                    layoutManager = LinearLayoutManager(mContext)
                    sentCommentsAdapter =
                        SentCommentsAdapter(mutableListOf(), this@CommentsFragment)
                    adapter = sentCommentsAdapter
                }
            }
            sentCommentsAdapter?.addComments(list)
            tvNoComments.isVisible = false
        }
    }

    override fun reportComment(commentId: String) {
        reportDialog = ReportDialog(R.string.report_comment) {
            val justSentComment = sentCommentsAdapter?.getComment(commentId)
            justSentComment?.let { viewModel.reportComment(dataId, it) }

            val comment = commentsAdapter?.getComment(commentId)
            comment?.let { viewModel.reportComment(dataId, it) }
        }
        activity?.let { reportDialog?.show(it.supportFragmentManager, "") }
    }

    private fun commentReported() {
        reportDialog?.dismiss()
        Toast.makeText(mContext, getString(R.string.report_finish), Toast.LENGTH_SHORT).show()
    }

    private fun error() {
        val dialog = ErrorDataDialog { activity?.onBackPressedDispatcher?.onBackPressed() }
        activity?.let { dialog.show(it.supportFragmentManager, "") }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
}

