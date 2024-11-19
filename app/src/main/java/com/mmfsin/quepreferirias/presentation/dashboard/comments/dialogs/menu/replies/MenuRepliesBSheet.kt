package com.mmfsin.quepreferirias.presentation.dashboard.comments.dialogs.menu.replies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.mmfsin.quepreferirias.base.BaseBottomSheet
import com.mmfsin.quepreferirias.databinding.BsheetMenuCommentBinding
import com.mmfsin.quepreferirias.domain.models.CommentReply
import com.mmfsin.quepreferirias.presentation.dashboard.comments.interfaces.ICommentMenuListener
import com.mmfsin.quepreferirias.presentation.models.DashboardType
import com.mmfsin.quepreferirias.utils.showErrorDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MenuRepliesBSheet(
    private val dataId: String,
    private val type: DashboardType,
    private val reply: CommentReply,
        private val listener: ICommentMenuListener
) : BaseBottomSheet<BsheetMenuCommentBinding>() {

    private val viewModel: MenuRepliesViewModel by viewModels()

    override fun inflateView(inflater: LayoutInflater) = BsheetMenuCommentBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe()
        viewModel.getSession()
    }

    override fun setUI() {
        binding.apply {
            btnAnswer.isVisible = false
            btnDelete.isVisible = false
        }
    }

    override fun setListeners() {
        binding.apply {
            btnDelete.setOnClickListener {
                viewModel.deleteReply(
                    dataId,
                    type,
                    reply.commentId,
                    reply.replyId
                )
            }
//            btnReport.setOnClickListener {
//                listener.reportComment(commentId)
//                dismiss()
//            }
        }
    }

    private fun observe() {
        viewModel.event.observe(this) { event ->
            when (event) {
                is MenuRepliesEvent.UserData -> {
                    event.session?.let { actualUser ->
                        if (reply.userId == actualUser.id) binding.btnDelete.isVisible = true
                    }
                }

                is MenuRepliesEvent.ReplyDeleted -> {
                    if (event.result) {
                        listener.replyDeleted(reply.commentId, reply.replyId)
                        dismiss()
                    } else error()
                }

                is MenuRepliesEvent.SWW -> error()
            }
        }
    }

    private fun error() {
        activity?.showErrorDialog { activity?.onBackPressedDispatcher?.onBackPressed() }
    }
}