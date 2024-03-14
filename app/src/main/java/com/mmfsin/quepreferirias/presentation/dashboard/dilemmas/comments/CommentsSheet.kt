package com.mmfsin.quepreferirias.presentation.dashboard.dilemmas.comments

import android.app.Dialog
import android.os.Bundle
import android.view.KeyEvent
import android.view.KeyEvent.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mmfsin.quepreferirias.R
import com.mmfsin.quepreferirias.databinding.BsheetCommentsBinding
import com.mmfsin.quepreferirias.domain.models.Comment
import com.mmfsin.quepreferirias.domain.models.CommentVote
import com.mmfsin.quepreferirias.domain.models.Session
import com.mmfsin.quepreferirias.presentation.dashboard.dilemmas.adapter.CommentsAdapter
import com.mmfsin.quepreferirias.presentation.dashboard.dilemmas.listener.IBSheetListener
import com.mmfsin.quepreferirias.presentation.dashboard.dilemmas.listener.ICommentsListener
import com.mmfsin.quepreferirias.utils.showErrorDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommentsSheet(private val dilemmaId: String, private val listener: IBSheetListener) :
    BottomSheetDialogFragment(), ICommentsListener {

    private val viewModel: CommentsViewModel by viewModels()

    private lateinit var binding: BsheetCommentsBinding

    private var hasSession = false
    private var userData: Session? = null
    private var commentsAdapter: CommentsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BsheetCommentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogThemeNoFloating)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.setOnShowListener {
            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let { parent ->
                val behaviour = BottomSheetBehavior.from(parent)
                setupFullHeight(parent)
                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        return dialog
    }

    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.checkSessionInitiated()
        observe()
        setListeners()
    }

    private fun setListeners() {
        binding.apply {
            ivSendComment.setOnClickListener {
                val comment = etComment.text.toString()
                if (comment.isNotBlank()) {
                    ivSendComment.isEnabled = false
                    ivSendComment.visibility = View.INVISIBLE
                    commentLoading.visibility = View.VISIBLE
                    userData?.let { viewModel.sendComment(dilemmaId, it, comment) }
                }
            }
        }
    }

    private fun observe() {
        viewModel.event.observe(this) { event ->
            when (event) {
                is CommentsEvent.InitiatedSession -> {
                    hasSession = event.initiatedSession
                    if (hasSession) viewModel.getUserData()
                    else {
                        binding.etComment.visibility = View.INVISIBLE
                        viewModel.getComments()
                    }
                }

                is CommentsEvent.GetUserData -> {
                    userData = event.data
                    activity?.let {
                        Glide.with(it.applicationContext).load(event.data.imageUrl)
                            .into(binding.image.image)
                        viewModel.getComments()
                    }
                }

                is CommentsEvent.Comments -> setUpComments(event.comments)
                is CommentsEvent.CommentSentResult -> commentSent()
                is CommentsEvent.CommentVotedResult -> updateCommentVotes(
                    event.vote,
                    event.position
                )

                is CommentsEvent.SWW -> error()
            }
        }
    }

    private fun commentSent() {
        binding.apply {
            ivSendComment.isEnabled = true
            ivSendComment.visibility = View.VISIBLE
            commentLoading.visibility = View.INVISIBLE
            etComment.text = null
            viewModel.getComments()
            listener.refreshComments()
        }
    }

    private fun setUpComments(comments: List<Comment>) {
        binding.apply {
            rvComments.apply {
                layoutManager = LinearLayoutManager(activity)
                commentsAdapter = CommentsAdapter(this@CommentsSheet, comments)
                adapter = commentsAdapter
            }
        }
    }

    private fun updateCommentVotes(vote: CommentVote, position: Int) {
        commentsAdapter?.updateCommentVotes(vote, position)
        listener.refreshComments()
    }

    override fun respondComment() {
    }

    override fun voteComment(commentId: String, vote: CommentVote, likes: Long, position: Int) {
        if (hasSession) viewModel.voteComment(dilemmaId, commentId, vote, likes, position)
        else {
            Toast.makeText(activity?.applicationContext, "no session", Toast.LENGTH_SHORT).show()
        }
    }

    private fun error() {
        activity?.showErrorDialog { activity?.onBackPressedDispatcher?.onBackPressed() }
    }
}