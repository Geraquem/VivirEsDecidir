package com.mmfsin.quepreferirias.presentation.saved.dualisms

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
import com.mmfsin.quepreferirias.domain.models.DualismFav
import com.mmfsin.quepreferirias.presentation.saved.dialogs.DeleteFavDataDialog
import com.mmfsin.quepreferirias.presentation.saved.dualisms.adapter.DualismsFavAdapter
import com.mmfsin.quepreferirias.presentation.saved.dualisms.interfaces.IDualismsFavListener
import com.mmfsin.quepreferirias.presentation.saved.listeners.ISavedDataListener
import com.mmfsin.quepreferirias.utils.showErrorDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DualismsFavFragment(val listener: ISavedDataListener) :
    BaseFragment<FragmentRvDataBinding, DualismsFavViewModel>(), IDualismsFavListener {

    override val viewModel: DualismsFavViewModel by viewModels()
    private lateinit var mContext: Context

    private var dialog: DeleteFavDataDialog? = null

    override fun inflateView(
        inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentRvDataBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getFavDualisms()
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
                viewModel.getFavDualisms()
                swipe.isRefreshing = false
            }
        }
    }

    override fun observe() {
        viewModel.event.observe(this) { event ->
            when (event) {
                is DualismsFavEvent.Data -> setDualismFavs(event.data)
                is DualismsFavEvent.FavDeleted -> {
                    dialog?.dismiss()
                    viewModel.getFavDualisms()
                }

                is DualismsFavEvent.SWW -> error()
            }
        }
    }

    private fun setDualismFavs(dualisms: List<DualismFav>) {
        binding.apply {
            val visible = dualisms.isEmpty()
            tvEmpty.isVisible = visible
            rvData.apply {
                layoutManager = LinearLayoutManager(mContext)
                adapter = DualismsFavAdapter(dualisms, this@DualismsFavFragment)
            }
            rvData.isVisible = !visible
            loading.root.visibility = View.GONE
        }
    }

    override fun onDualismFavClick(dualismId: String) = listener.navigateToSingleDualism(dualismId)

    override fun onDualismFavLongClick(dualismId: String) {
        dialog = DeleteFavDataDialog { viewModel.deleteDualismFav(dualismId) }
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