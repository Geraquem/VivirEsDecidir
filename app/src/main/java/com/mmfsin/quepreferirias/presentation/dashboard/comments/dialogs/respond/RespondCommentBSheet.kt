package com.mmfsin.quepreferirias.presentation.dashboard.comments.dialogs.respond

import android.content.Context.INPUT_METHOD_SERVICE
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mmfsin.quepreferirias.R
import com.mmfsin.quepreferirias.base.BaseBottomSheet
import com.mmfsin.quepreferirias.databinding.BsheetSendCommentBinding
import com.mmfsin.quepreferirias.domain.models.DataToRespondComment
import com.mmfsin.quepreferirias.domain.models.Session
import com.mmfsin.quepreferirias.presentation.dashboard.comments.interfaces.ICommentsRVListener
import com.mmfsin.quepreferirias.utils.showErrorDialog
import com.mmfsin.quepreferirias.utils.showKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RespondCommentBSheet(
    private val data: DataToRespondComment,
    private val session: Session,
    private val listener: ICommentsRVListener,
) : BaseBottomSheet<BsheetSendCommentBinding>() {

    private val viewModel: RespondCommentViewModel by viewModels()

    override fun inflateView(inflater: LayoutInflater) = BsheetSendCommentBinding.inflate(inflater)

    override fun onStart() {
        super.onStart()
        observe()

        val bottomSheet =
            dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.let {
            val behavior = BottomSheetBehavior.from(it)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.skipCollapsed = true

            val layoutParams = it.layoutParams
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            it.layoutParams = layoutParams
        }
    }

    override fun setUI() {
        binding.apply {
            rlLoading.isVisible = false
            tvSendComment.text = getString(R.string.comment_reply)
            tvData.text = data.commentText
            etComment.postDelayed({
                etComment.requestFocus()
                activity?.showKeyboard(etComment)
            }, 100)
            setTextWatcherComment()
        }
    }

    private fun setTextWatcherComment() {
        val comment = binding.etComment
        comment.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val text = comment.text.toString()
                val filteredText = text.replace("\n\n\n", "\n\n")
                if (filteredText != text) {
                    comment.setText(filteredText)
                    comment.setSelection(filteredText.length)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    override fun setListeners() {
        binding.apply {
            ivClose.setOnClickListener { dismiss() }
            tvSendComment.setOnClickListener {
                val comment = etComment.text.toString()
                if (comment.isNotBlank()) {
                    val filteredText = comment.replace("\n\n\n", "\n\n")
                    viewModel.respondComment(
                        dataId = data.dataId,
                        commentId = data.commentId,
                        type = data.type,
                        session = session,
                        reply = filteredText
                    )
                    rlLoading.isVisible = true
                    hideKeyboard()
                }
            }
        }
    }


    private fun hideKeyboard() {
        val view = dialog?.currentFocus ?: view
        view?.let {
            val inputMethodManager =
                activity?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    private fun observe() {
        viewModel.event.observe(this) { event ->
            when (event) {
                is RespondCommentEvent.CommentReplied -> {
                    listener.respondComment(data.commentId, event.reply)
                    dismiss()
                }

                is RespondCommentEvent.SWW -> error()
            }
        }
    }

    private fun error() {
        activity?.showErrorDialog { activity?.onBackPressedDispatcher?.onBackPressed() }
    }
}