package com.mmfsin.quepreferirias.presentation.create

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mmfsin.quepreferirias.databinding.FragmentSendQuestionsBinding

class SendQuestionsFragment : Fragment(), SendQuestionView {

    private var _bdg: FragmentSendQuestionsBinding? = null
    private val binding get() = _bdg!!

    private val presenter by lazy { SendQuestionsPresenter(this) }

    private lateinit var mContext: Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _bdg = FragmentSendQuestionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun everythingOk() {
    }

    override fun sww() {

    }
}