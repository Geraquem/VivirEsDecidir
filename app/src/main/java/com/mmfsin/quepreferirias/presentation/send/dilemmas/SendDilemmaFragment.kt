package com.mmfsin.quepreferirias.presentation.send.dilemmas

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.mmfsin.quepreferirias.R
import com.mmfsin.quepreferirias.base.BaseFragment
import com.mmfsin.quepreferirias.databinding.FragmentSendDilemmaBinding
import com.mmfsin.quepreferirias.domain.models.Session
import com.mmfsin.quepreferirias.presentation.main.BedRockActivity
import com.mmfsin.quepreferirias.presentation.send.dilemmas.interfaces.TextWatcher
import com.mmfsin.quepreferirias.utils.showErrorDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SendDilemmaFragment : BaseFragment<FragmentSendDilemmaBinding, SendDilemmaViewModel>() {

    override val viewModel: SendDilemmaViewModel by viewModels()
    private lateinit var mContext: Context


    override fun inflateView(
        inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentSendDilemmaBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getUserData()
    }

    override fun setUI() {
        binding.apply {
            setToolbar()
            loading.root.isVisible = true
            etTop.setOptions()
            tvLimitTop.text = getString(R.string.send_dilemma_txt_empty)
            etBottom.setOptions()
            tvLimitBottom.text = getString(R.string.send_dilemma_txt_empty)
        }
    }

    private fun setToolbar() {
        (activity as BedRockActivity).apply {
            backListener { onBackPressed() }
            setToolbarText(R.string.send_dilemma_title)
        }
    }

    override fun setListeners() {
        binding.apply {
            TextWatcher().addTextWatcher(etTop, object : TextWatcher.TextWatcherListener {
                override fun onTextChanged(text: String) {
                    tvLimitTop.text = getString(R.string.send_dilemma_txt_max, text)
                }
            })

            TextWatcher().addTextWatcher(etBottom, object : TextWatcher.TextWatcherListener {
                override fun onTextChanged(text: String) {
                    tvLimitBottom.text = getString(R.string.send_dilemma_txt_max, text)
                }
            })

            btnSend.setOnClickListener {  }
        }
    }

    private fun EditText.setOptions() {
        this.imeOptions = EditorInfo.IME_ACTION_DONE;
        this.setRawInputType(InputType.TYPE_CLASS_TEXT);
    }

    override fun observe() {
        viewModel.event.observe(this) { event ->
            when (event) {
                is SendDilemmaEvent.UserData -> setUserData(event.session)
                is SendDilemmaEvent.Result -> {}
                is SendDilemmaEvent.SWW -> error()
            }
        }
    }

    private fun setUserData(data: Session) {
        binding.apply {
            Glide.with(mContext).load(data.imageUrl).into(ivImage.image)
            tvName.text = data.name
            loading.root.visibility = View.GONE
        }
    }

    private fun error() {
        activity?.showErrorDialog { activity?.finish() }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
}