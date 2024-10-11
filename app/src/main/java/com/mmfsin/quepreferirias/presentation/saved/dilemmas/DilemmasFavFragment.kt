package com.mmfsin.quepreferirias.presentation.saved.dilemmas

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mmfsin.quepreferirias.R
import com.mmfsin.quepreferirias.base.BaseFragment
import com.mmfsin.quepreferirias.databinding.FragmentRvDataBinding
import com.mmfsin.quepreferirias.domain.models.DilemmaFav
import com.mmfsin.quepreferirias.presentation.saved.dialogs.DeleteFavDataDialog
import com.mmfsin.quepreferirias.presentation.saved.dilemmas.adapter.DilemmasFavAdapter
import com.mmfsin.quepreferirias.presentation.saved.dilemmas.interfaces.IDilemmasFavListener
import com.mmfsin.quepreferirias.presentation.saved.listeners.ISavedDataListener
import com.mmfsin.quepreferirias.utils.showErrorDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DilemmasFavFragment(val listener: ISavedDataListener) :
    BaseFragment<FragmentRvDataBinding, DilemmasFavViewModel>(), IDilemmasFavListener {

    override val viewModel: DilemmasFavViewModel by viewModels()
    private lateinit var mContext: Context

    private var dialog: DeleteFavDataDialog? = null

    override fun inflateView(
        inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentRvDataBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getFavDilemmas()
    }

    override fun setUI() {
        binding.apply {
            loading.root.isVisible
            tvEmpty.text = getString(R.string.saved_data_empty)
        }
    }

    override fun setListeners() {
        binding.apply {
            swipe.setOnRefreshListener {
                viewModel.getFavDilemmas()
                swipe.isRefreshing = false
            }
        }
    }

    override fun observe() {
        viewModel.event.observe(this) { event ->
            when (event) {
                is DilemmasFavEvent.Data -> setDilemmaFavs(event.data)
                is DilemmasFavEvent.FavDeleted -> {
                    dialog?.dismiss()
                    viewModel.getFavDilemmas()
                }

                is DilemmasFavEvent.SWW -> error()
            }
        }
    }

    private fun setDilemmaFavs(dilemmas: List<DilemmaFav>) {
        binding.apply {
            val visible = dilemmas.isEmpty()
            tvEmpty.isVisible = visible
            rvData.apply {
                layoutManager = LinearLayoutManager(mContext)
                adapter = DilemmasFavAdapter(dilemmas, this@DilemmasFavFragment)
            }
            rvData.isVisible = !visible
            loading.root.visibility = View.GONE
        }
    }

    override fun onDilemmaFavClick(dilemmaId: String) = listener.navigateToSingleDilemma(dilemmaId)

    override fun onDilemmaFavLongClick(dilemmaId: String) {
        dialog = DeleteFavDataDialog { viewModel.deleteDilemmaFav(dilemmaId) }
        activity?.let { dialog?.show(it.supportFragmentManager, "") }
    }

    private fun error() {
        activity?.showErrorDialog { activity?.finish() }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
}