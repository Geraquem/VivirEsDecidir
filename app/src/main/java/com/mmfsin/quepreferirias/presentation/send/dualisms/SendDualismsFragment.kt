package com.mmfsin.quepreferirias.presentation.send.dualisms

import android.content.Context
import android.os.Bundle
import android.text.InputType
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
import com.mmfsin.quepreferirias.databinding.FragmentSendDualismBinding
import com.mmfsin.quepreferirias.domain.models.Session
import com.mmfsin.quepreferirias.presentation.main.BedRockActivity
import com.mmfsin.quepreferirias.presentation.send.dialogs.SendDataResultDialog
import com.mmfsin.quepreferirias.presentation.send.interfaces.ISendDataResultListener
import com.mmfsin.quepreferirias.presentation.send.interfaces.TextWatcher
import com.mmfsin.quepreferirias.utils.showErrorDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SendDualismsFragment : BaseFragment<FragmentSendDualismBinding, SendDualismsViewModel>(),
    ISendDataResultListener {

    override val viewModel: SendDualismsViewModel by viewModels()
    private lateinit var mContext: Context

    private var session: Session? = null

    override fun inflateView(
        inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentSendDualismBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getUserData()
    }

    override fun setUI() {
        binding.apply {
            setToolbar()
            loadingFull.root.visibility = View.VISIBLE
            loadingTrans.root.visibility = View.GONE

            swExplanation.isChecked = false
            llExplanation.isVisible = false
            etTop.hint = getString(R.string.send_dualism_only_option_one_hint)
            etBottom.hint = getString(R.string.send_dualism_only_option_two_hint)

            tvError.visibility = View.GONE
            etExplanation.setOptions()
            tvLimitExplanation.text = getString(R.string.send_dualism_explanation_empty)
            etTop.setOptions()
            tvLimitTop.text = getString(R.string.send_dilemma_txt_empty)
            etBottom.setOptions()
            tvLimitBottom.text = getString(R.string.send_dilemma_txt_empty)
        }
    }

    private fun setToolbar() {
        (activity as BedRockActivity).apply {
            backListener { onBackPressed() }
            setToolbarText(R.string.send_dualism_title)
        }
    }

    override fun setListeners() {
        binding.apply {
            swExplanation.setOnClickListener {
                llExplanation.isVisible = swExplanation.isChecked
                if (swExplanation.isChecked) {
                    etTop.hint = getString(R.string.send_dualism_option_one_hint)
                    etBottom.hint = getString(R.string.send_dualism_option_two_hint)
                } else {
                    etTop.hint = getString(R.string.send_dualism_only_option_one_hint)
                    etBottom.hint = getString(R.string.send_dualism_only_option_two_hint)
                }
            }

            TextWatcher().addTextWatcher(etExplanation, object : TextWatcher.TextWatcherListener {
                override fun onTextChanged(text: String) {
                    tvLimitExplanation.text = getString(R.string.send_dualism_explanation_max, text)
                }
            })

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

            btnSend.setOnClickListener {
                session?.let { user ->
                    val txtTop = etTop.text.toString()
                    val txtBottom = etBottom.text.toString()

                    val expText = etExplanation.text.toString()
                    var explanation: String? = null
                    if (expText.isNotEmpty()) explanation = expText

                    if (txtTop.isNotBlank() && txtBottom.isNotBlank()) {
                        tvError.visibility = View.GONE
                        loadingTrans.root.visibility = View.VISIBLE
                        viewModel.sendDualism(explanation, txtTop, txtBottom, user.id, user.name)
                    } else tvError.visibility = View.VISIBLE
                } ?: run { error() }
            }
        }
    }

    private fun EditText.setOptions() {
        this.imeOptions = EditorInfo.IME_ACTION_DONE
        this.setRawInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES)
    }

    override fun observe() {
        viewModel.event.observe(this) { event ->
            when (event) {
                is SendDualismsEvent.UserData -> setUserData(event.session)
                is SendDualismsEvent.DataSent -> setResult(true)
                is SendDualismsEvent.SWW -> setResult(false)
            }
        }
    }

    private fun setResult(result: Boolean) {
        val dialog = SendDataResultDialog(result, this@SendDualismsFragment)
        activity?.let { dialog.show(it.supportFragmentManager, "") }
        binding.loadingTrans.root.visibility = View.GONE
    }

    private fun setUserData(data: Session) {
        session = data
        binding.apply {
            Glide.with(mContext).load(data.imageUrl).into(ivImage.image)
            tvName.text = data.name
            loadingFull.root.visibility = View.GONE
        }
    }

    private fun error() {
        activity?.showErrorDialog { activity?.finish() }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun sendAnother() {
        binding.apply {
            etExplanation.text = null
            etTop.text = null
            etBottom.text = null
        }
    }

    override fun close() {
        activity?.onBackPressedDispatcher?.onBackPressed()
    }
}