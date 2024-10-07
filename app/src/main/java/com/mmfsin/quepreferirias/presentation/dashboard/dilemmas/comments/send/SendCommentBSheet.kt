package com.mmfsin.quepreferirias.presentation.dashboard.dilemmas.comments.send

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mmfsin.quepreferirias.R
import com.mmfsin.quepreferirias.databinding.BsheetSendCommentBinding
import com.mmfsin.quepreferirias.domain.models.Dilemma
import com.mmfsin.quepreferirias.presentation.dashboard.dilemmas.listener.ISendCommentListener
import com.mmfsin.quepreferirias.utils.showErrorDialog
import com.mmfsin.quepreferirias.utils.showKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SendCommentBSheet(
    private val dilemma: Dilemma,
    private val listener: ISendCommentListener
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
            tvDilemma.text = getString(
                R.string.comments_dilemma,
                dilemma.txtTop,
                dilemma.txtBottom
            )
            etComment.postDelayed({
                etComment.requestFocus()
                activity?.showKeyboard(etComment)
            }, 100)
        }
    }

    private fun setListeners() {
        binding.apply {
            ivClose.setOnClickListener { dismiss() }
            tvSendComment.setOnClickListener { }
        }
    }

    private fun observe() {
        viewModel.event.observe(this) { event ->
            when (event) {
                is SendCommentEvent.CommentSent -> {
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