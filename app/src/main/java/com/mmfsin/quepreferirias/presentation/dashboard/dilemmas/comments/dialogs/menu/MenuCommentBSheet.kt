package com.mmfsin.quepreferirias.presentation.dashboard.dilemmas.comments.dialogs.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mmfsin.quepreferirias.databinding.BsheetMenuCommentBinding
import com.mmfsin.quepreferirias.presentation.dashboard.dilemmas.interfaces.ICommentMenuListener
import com.mmfsin.quepreferirias.utils.showErrorDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MenuCommentBSheet(
    private val dilemmaId: String,
    private val commentId: String,
    private val userId: String,
    private val listener: ICommentMenuListener
) : BottomSheetDialogFragment() {

    private var _binding: BsheetMenuCommentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MenuCommentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BsheetMenuCommentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUI()
        setListeners()
        observe()

        viewModel.getSession()
    }

    private fun setUI() {
        binding.btnDelete.isVisible = false
    }

    private fun setListeners() {
        binding.apply {
            btnAnswer.setOnClickListener {
                listener.respondComment(commentId)
                dismiss()
            }
            btnDelete.setOnClickListener { viewModel.deleteComment(dilemmaId, commentId) }
            btnReport.setOnClickListener {
                listener.reportComment(commentId)
                dismiss()
            }
        }
    }

    private fun observe() {
        viewModel.event.observe(this) { event ->
            when (event) {
                is MenuCommentEvent.UserData -> {
                    event.session?.let { actualUser ->
                        if (userId == actualUser.id) binding.btnDelete.isVisible = true
                    }
                }

                is MenuCommentEvent.CommentDeleted -> {
                    if (event.result) {
                        listener.commentDeleted(commentId)
                        dismiss()
                    } else error()
                }

                is MenuCommentEvent.SWW -> error()
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