package com.mmfsin.quepreferirias.presentation.dashboard.comments.dialogs.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.mmfsin.quepreferirias.base.BaseBottomSheet
import com.mmfsin.quepreferirias.databinding.BsheetMenuCommentBinding
import com.mmfsin.quepreferirias.domain.models.DataToRespondComment
import com.mmfsin.quepreferirias.presentation.dashboard.comments.interfaces.ICommentMenuListener
import com.mmfsin.quepreferirias.presentation.models.DashboardType
import com.mmfsin.quepreferirias.utils.showErrorDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MenuCommentBSheet(
    private val dataId: String,
    private val type: DashboardType,
    private val commentId: String,
    private val commentText: String,
    private val userId: String,
    private val listener: ICommentMenuListener
) : BaseBottomSheet<BsheetMenuCommentBinding>() {

    private val viewModel: MenuCommentViewModel by viewModels()

    override fun inflateView(inflater: LayoutInflater) = BsheetMenuCommentBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe()
        viewModel.getSession()
    }

    override fun setUI() {
        binding.btnDelete.isVisible = false
    }

    override fun setListeners() {
        binding.apply {
            btnAnswer.setOnClickListener {
                val data = DataToRespondComment(dataId, type, commentId, commentText)
                listener.respondComment(data)
                dismiss()
            }
            btnDelete.setOnClickListener { viewModel.deleteComment(dataId, type, commentId) }
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
}