package com.mmfsin.quepreferirias.presentation.dashboard.dilemmas.comments.send

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mmfsin.quepreferirias.databinding.BsheetSendCommentBinding
import com.mmfsin.quepreferirias.domain.models.Session
import com.mmfsin.quepreferirias.presentation.dashboard.dilemmas.listener.ISendCommentListener
import com.mmfsin.quepreferirias.utils.showErrorDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SendCommentBSheet(
    private val session: Session,
    private val listener: ISendCommentListener
) : BottomSheetDialogFragment() {

    private var _binding: BsheetSendCommentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SendCommentViewModel by viewModels()

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
    }

    private fun setListeners() {
        binding.apply {
        }
    }

    private fun observe(){
        viewModel.event.observe(this) { event ->
            when (event) {
                is SendCommentEvent.CommentSent-> {
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