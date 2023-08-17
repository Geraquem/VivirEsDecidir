package com.mmfsin.quepreferirias.presentation.saved

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mmfsin.quepreferirias.R
import com.mmfsin.quepreferirias.base.BaseFragment
import com.mmfsin.quepreferirias.databinding.FragmentSavedDataBinding
import com.mmfsin.quepreferirias.domain.models.SavedData
import com.mmfsin.quepreferirias.presentation.main.BedRockActivity
import com.mmfsin.quepreferirias.presentation.saved.adapter.SavedDataAdapter
import com.mmfsin.quepreferirias.presentation.saved.interfaces.ISavedDataListener
import com.mmfsin.quepreferirias.utils.showErrorDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SavedDataFragment : BaseFragment<FragmentSavedDataBinding, SavedDataViewModel>(),
    ISavedDataListener {

    override val viewModel: SavedDataViewModel by viewModels()
    private lateinit var mContext: Context

    private var savedDataList: List<SavedData>? = null

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentSavedDataBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getSavedData()
    }

    private fun setToolbar() {
        (activity as BedRockActivity).apply {
            backListener { onBackPressed() }
            setToolbarText(R.string.saved_data_title)
        }
    }

    override fun setUI() {
        setToolbar()
        binding.apply {
            loading.root.visibility = View.VISIBLE
            savedDataList?.let { list ->
                tvEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
                rvSavedData.apply {
                    layoutManager = LinearLayoutManager(mContext)
                    adapter = SavedDataAdapter(list, this@SavedDataFragment)
                }
                loading.root.visibility = View.GONE
            }
        }
    }

    override fun onDataClicked(dataId: String) {

    }

    override fun setListeners() {
        binding.apply {}
    }

    override fun observe() {
        viewModel.event.observe(this) { event ->
            when (event) {
                is SavedDataEvent.Data -> {
                    savedDataList = event.data
                    setUI()
                }
                is SavedDataEvent.SWW -> activity?.showErrorDialog { }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
}