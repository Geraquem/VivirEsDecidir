package com.mmfsin.quepreferirias.presentation.create

import androidx.fragment.app.Fragment

class SendQuestionsFragment : Fragment() {
}
//
//    private var _bdg: FragmentSendQuestionsBinding? = null
//    private val binding get() = _bdg!!
//
//    private val presenter by lazy { SendQuestionsRepositoryPresenter(this) }
//
//    private lateinit var mContext: Context
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
//    ): View {
//        _bdg = FragmentSendQuestionsBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        setListeners()
//    }
//
//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        mContext = context
//    }
//
//    private fun setListeners() {
//        binding.apply {
//            ivBack.setOnClickListener { activity?.onBackPressed() }
//            btnSend.setOnClickListener {
//                val textA = txtTop.text.toString()
//                val textB = txtBottom.text.toString()
//                if (textA.isNotEmpty() && textB.isNotEmpty()) sendQuestion(textA, textB)
//            }
//
//            btnMoreApps.setOnClickListener {
//                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.mmfsinURL))))
//            }
//        }
//    }
//
//    private fun sendQuestion(txtA: String, txtB: String) {
//        closeKeyboard()
//        binding.result.apply {
//            everythingOk.visibility = View.INVISIBLE
//            everythingKo.visibility = View.INVISIBLE
//            loading.visibility = View.VISIBLE
//        }
//        binding.btnSend.isEnabled = false
//        val creatorName = binding.creatorName.text.toString()
//        val data = if (creatorName.isEmpty()) QuestionSentDTO(txtA, txtB)
//        else QuestionSentDTO(txtA, txtB, creatorName)
//        presenter.sendQuestion(data)
//    }
//
//    override fun result(result: Boolean) = if (result) okFlow() else koFlow()
//
//    private fun okFlow() {
//        binding.apply {
//            result.apply {
//                loading.visibility = View.INVISIBLE
//                everythingOk.visibility = View.VISIBLE
//                lottieCheck.playAnimation()
//            }
//            txtTop.text = null
//            txtBottom.text = null
//            btnSend.isEnabled = true
//        }
//    }
//
//    private fun koFlow() {
//        binding.apply {
//            result.apply {
//                loading.visibility = View.INVISIBLE
//                everythingKo.visibility = View.VISIBLE
//                lottieError.playAnimation()
//            }
//            btnSend.isEnabled = true
//        }
//    }
//
//    private fun closeKeyboard() {
//        activity?.let { act ->
//            act.currentFocus?.let { view ->
//                val imm =
//                    act.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//                imm.hideSoftInputFromWindow(view.windowToken, 0)
//            }
//        }
//    }
//}