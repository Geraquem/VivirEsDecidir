package com.mmfsin.quepreferirias.presentation.dashboard.dilemmas.comments

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
import com.mmfsin.quepreferirias.domain.models.CommentVote
import com.mmfsin.quepreferirias.domain.models.Session
import com.mmfsin.quepreferirias.presentation.dashboard.dilemmas.comments.adapter.CommentsAdapter
import com.mmfsin.quepreferirias.presentation.dashboard.dilemmas.listener.ICommentsRVListener
import com.mmfsin.quepreferirias.presentation.dashboard.dilemmas.listener.ICommentsListener
import com.mmfsin.quepreferirias.presentation.single.dialogs.ErrorDataDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommentsFragment(val dilemmaId: String, val listener: ICommentsListener) :
    BaseFragment<FragmentCommentsBinding, CommentsViewModel>(), ICommentsRVListener {

    override val viewModel: CommentsViewModel by viewModels()
    private lateinit var mContext: Context

    private var hasSession = false
    private var userData: Session? = null
    private var commentsAdapter: CommentsAdapter? = null

    override fun inflateView(
        inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentCommentsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.checkSessionInitiated()
    }

    override fun setUI() {
        binding.apply {
            loadingMore.isVisible = false
        }
    }

    override fun observe() {
        viewModel.event.observe(this) { event ->
            when (event) {
                is CommentsEvent.InitiatedSession -> {
                    hasSession = event.initiatedSession
                    if (hasSession) viewModel.getUserData()
                    else viewModel.getComments(dilemmaId)
                }

                is CommentsEvent.UserData -> {
                    userData = event.data
                    setUserData()
                    viewModel.getComments(dilemmaId)
                }

                is CommentsEvent.Comments -> setUpComments(event.comments)
                is CommentsEvent.CommentSentResult -> {} //commentSent()
                is CommentsEvent.CommentAlreadyVoted -> {
                    Toast.makeText(
                        activity?.applicationContext,
                        getString(R.string.comment_already_voted),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is CommentsEvent.CommentVotedResult -> {
//                    updateCommentVotes(
//                        event.vote,
//                        event.position,
//                        event.alreadyVoted
//                    )
                }

                is CommentsEvent.SWW -> error()
            }
        }
    }

    private fun setUserData() {
        binding.apply { }
    }

    private fun setUpComments(comments: List<Comment>) {
        binding.apply {
            if (rvComments.adapter == null) {
                tvNoComments.isVisible = comments.isEmpty()
                rvComments.apply {
                    layoutManager = LinearLayoutManager(mContext)
                    commentsAdapter = CommentsAdapter(
                        comments as MutableList<Comment>,
                        this@CommentsFragment
                    )
                    adapter = commentsAdapter
                }
            } else commentsAdapter?.addComments(comments)
            loadingMore.isVisible = false
            loadingComments.isVisible = false
        }
    }

    override fun respondComment() {
        //TODO
    }

    override fun onCommentNameClick(userId: String) = listener.navigateToUserProfile(userId)

    override fun voteComment(
        commentId: String,
        vote: CommentVote,
        likes: Long,
        position: Int
    ) {
        if (hasSession) viewModel.voteComment(dilemmaId, commentId, vote, likes, position)
        else {
            Toast.makeText(activity?.applicationContext, "no session", Toast.LENGTH_SHORT)
                .show()
        }
    }

    fun updateComments() {
        binding.loadingMore.isVisible = true
        viewModel.getComments(dilemmaId)
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

