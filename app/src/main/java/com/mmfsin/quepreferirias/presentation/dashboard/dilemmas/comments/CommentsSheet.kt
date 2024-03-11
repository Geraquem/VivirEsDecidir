package com.mmfsin.quepreferirias.presentation.dashboard.dilemmas.comments

import android.app.Dialog
import android.os.Bundle
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
import com.mmfsin.quepreferirias.presentation.dashboard.dilemmas.listener.ICommentsListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommentsSheet(private val dilemmaId: String, private val comments: List<Comment>) :
    BottomSheetDialogFragment(), ICommentsListener {

    private val viewModel: CommentsViewModel by viewModels()

    private lateinit var binding: BsheetCommentsBinding
    private var userData: Session? = null

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
        viewModel.getUserData()
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
                is CommentsEvent.GetUserData -> {
                    userData = event.data
                    activity?.let {
                        Glide.with(it.applicationContext).load(event.data.imageUrl)
                            .into(binding.image.image)
                        setUpComments()
                    }
                }

                is CommentsEvent.CommentResult -> commentResult(event.result)

                is CommentsEvent.SWW -> {}
            }
        }
    }

    private fun commentResult(success: Boolean) {
        binding.apply {
            ivSendComment.isEnabled = true
            ivSendComment.visibility = View.VISIBLE
            commentLoading.visibility = View.INVISIBLE
            etComment.text = null
        }

        Toast.makeText(
            activity?.applicationContext,
            success.toString(),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun setUpComments() {
        binding.apply {
            rvComments.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = CommentsAdapter(this@CommentsSheet, comments)
            }
        }
    }

    override fun addNewComment() {
    }

    override fun respondComment() {
    }

    override fun voteComment(vote: CommentVote, comment: Comment) {
//        viewModel.voteComment(dilemmaId, vote, comment)
    }
}