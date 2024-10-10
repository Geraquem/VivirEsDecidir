package com.mmfsin.quepreferirias.presentation.dashboard.comments.dialogs.send

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mmfsin.quepreferirias.R
import com.mmfsin.quepreferirias.databinding.BsheetSendCommentBinding
import com.mmfsin.quepreferirias.domain.models.Dilemma
import com.mmfsin.quepreferirias.domain.models.Dualism
import com.mmfsin.quepreferirias.domain.models.Session
import com.mmfsin.quepreferirias.presentation.dashboard.dilemmas.interfaces.ISendCommentListener
import com.mmfsin.quepreferirias.presentation.models.DashboardType
import com.mmfsin.quepreferirias.presentation.models.DashboardType.DILEMMA
import com.mmfsin.quepreferirias.presentation.models.DashboardType.DUALISM
import com.mmfsin.quepreferirias.utils.showErrorDialog
import com.mmfsin.quepreferirias.utils.showKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SendCommentBSheet(
    private val data: Any,
    private val type: DashboardType,
    private val session: Session,
    private val listener: ISendCommentListener,
) : BottomSheetDialogFragment() {

    private var _binding: BsheetSendCommentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SendCommentViewModel by viewModels()

    override fun onStart() {
        super.onStart()

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BsheetSendCommentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUI()
        setListeners()
        observe()
    }

    private fun setUI() {
        binding.apply {
            rvLoading.isVisible = false
            tvDilemma.text = getDataText()
            etComment.postDelayed({
                etComment.requestFocus()
                activity?.showKeyboard(etComment)
            }, 100)
            setTextWatcherComment()
        }
    }

    private fun getDataText(): String {
        return when (type) {
            DILEMMA -> {
                (data as Dilemma)
                getString(R.string.comments_dilemma, data.txtTop, data.txtBottom)
            }

            DUALISM -> {
                (data as Dualism)
                getString(R.string.comments_dualism, data.txtTop, data.txtBottom)
            }
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

    private fun setListeners() {
        binding.apply {
            ivClose.setOnClickListener { dismiss() }
            tvSendComment.setOnClickListener {
                val comment = etComment.text.toString()
                if (comment.isNotBlank()) {
                    val filteredText = comment.replace("\n\n\n", "\n\n")
                    viewModel.sendComment(getDataId(), type, session, filteredText)
                    rvLoading.isVisible = true
                    hideKeyboard()
                }
            }
        }
    }

    private fun getDataId(): String {
        return when (type) {
            DILEMMA -> (data as Dilemma).id
            DUALISM -> (data as Dualism).id
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
                is SendCommentEvent.CommentSent -> {
                    listener.commentSent(event.comment)
                    dismiss()
                }

                is SendCommentEvent.SWW -> error()
            }
        }
    }

    private fun error() {
        activity?.showErrorDialog { activity?.onBackPressedDispatcher?.onBackPressed() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}