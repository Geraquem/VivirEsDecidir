package com.mmfsin.quepreferirias.presentation.sendquestions

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.viewModels
import com.mmfsin.quepreferirias.base.BaseFragment
import com.mmfsin.quepreferirias.databinding.FragmentSendQuestionsBinding
import com.mmfsin.quepreferirias.presentation.sendquestions.dialog.SqResultDialog
import com.mmfsin.quepreferirias.utils.showErrorDialog
import com.mmfsin.quepreferirias.utils.toEditable
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SendQuestionsFragment : BaseFragment<FragmentSendQuestionsBinding, SendQuestionsViewModel>() {

    override val viewModel: SendQuestionsViewModel by viewModels()

    private lateinit var mContext: Context

    override fun inflateView(
        inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentSendQuestionsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getCreatorName()
    }

    override fun setUI() {
        binding.apply {
            loading.visibility = View.GONE
        }
    }

    override fun setListeners() {
        binding.apply {
            ivBack.setOnClickListener { activity?.onBackPressed() }

            btnSend.setOnClickListener {
                val textTop = etTextTop.text.toString()
                val textBottom = etTextBottom.text.toString()
                val creatorName = etCreatorName.text.toString()
                if (textTop.isNotEmpty() && textBottom.isNotEmpty()) {
                    loading.visibility = View.VISIBLE
                    viewModel.sendQuestion(textTop, textBottom, creatorName)
                }
            }
        }
    }

    override fun observe() {
        viewModel.event.observe(this) { event ->
            when (event) {
                is SendQuestionsEvent.CreatorName -> setCreatorName(event.name)
                is SendQuestionsEvent.SendQuestionResult -> setResult(event.result)
                is SendQuestionsEvent.SWW -> activity?.showErrorDialog { }
            }
        }
    }

    private fun setCreatorName(name: String?) {
        val creatorName = binding.etCreatorName
        name?.let { creatorName.text = it.toEditable() }
            ?: run { creatorName.text = null }
    }

    private fun setResult(questionResult: Boolean) {
        binding.apply {
            val dialog = SqResultDialog(questionResult) { resetEditTexts() }
            activity?.let { dialog.show(it.supportFragmentManager, "") }
            loading.visibility = View.GONE
        }
        closeKeyboard()
    }

    private fun resetEditTexts() {
        binding.apply {
            etTextTop.text = null
            etTextBottom.text = null
        }
    }

    private fun closeKeyboard() {
        activity?.let { act ->
            act.currentFocus?.let { view ->
                val imm =
                    act.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
}