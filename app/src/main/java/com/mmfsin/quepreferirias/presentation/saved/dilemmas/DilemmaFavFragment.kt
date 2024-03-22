package com.mmfsin.quepreferirias.presentation.saved.dilemmas

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mmfsin.quepreferirias.R
import com.mmfsin.quepreferirias.base.BaseFragment
import com.mmfsin.quepreferirias.databinding.FragmentRvDataBinding
import com.mmfsin.quepreferirias.domain.models.DilemmaFav
import com.mmfsin.quepreferirias.presentation.saved.delete.DeleteFavDataDialog
import com.mmfsin.quepreferirias.presentation.saved.dilemmas.adapter.DilemmaFavsAdapter
import com.mmfsin.quepreferirias.presentation.saved.dilemmas.interfaces.IDilemmaFavListener
import com.mmfsin.quepreferirias.utils.showErrorDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DilemmaFavFragment : BaseFragment<FragmentRvDataBinding, DilemmaFavViewModel>(),
    IDilemmaFavListener {

    override val viewModel: DilemmaFavViewModel by viewModels()
    private lateinit var mContext: Context

    private var dialog: DeleteFavDataDialog? = null

    override fun inflateView(
        inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentRvDataBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getFavDilemmasData()
    }

    override fun setUI() {
        binding.apply {
            loading.root.isVisible
            tvEmpty.text = getString(R.string.saved_data_empty)
        }
    }

    override fun setListeners() {
        binding.apply {
        }
    }

    override fun observe() {
        viewModel.event.observe(this) { event ->
            when (event) {
                is DilemmaFavEvent.Data -> setDilemmaFavs(event.data)
                is DilemmaFavEvent.FavDeleted -> {
                    dialog?.dismiss()
                    viewModel.getFavDilemmasData()
                }

                is DilemmaFavEvent.SWW -> error()
            }
        }
    }

    private fun setDilemmaFavs(dilemmas: List<DilemmaFav>) {
        binding.apply {
            val visible = dilemmas.isEmpty()
            tvEmpty.isVisible = visible
            rvData.apply {
                layoutManager = LinearLayoutManager(mContext)
                adapter = DilemmaFavsAdapter(dilemmas, this@DilemmaFavFragment)
            }
            rvData.isVisible = !visible
            loading.root.visibility = View.GONE
        }
    }

    override fun onDilemmaFavClick(dilemmaId: String) {
        Toast.makeText(mContext, dilemmaId, Toast.LENGTH_SHORT).show()
    }

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