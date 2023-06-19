package com.mmfsin.quepreferirias.presentation.dashboard

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.mmfsin.quepreferirias.base.BaseFragment
import com.mmfsin.quepreferirias.databinding.FragmentDashboardBinding
import com.mmfsin.quepreferirias.domain.models.Data
import com.mmfsin.quepreferirias.presentation.MainActivity
import com.mmfsin.quepreferirias.presentation.models.Percents
import com.mmfsin.quepreferirias.utils.showErrorDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment : BaseFragment<FragmentDashboardBinding, DashboardViewModel>() {

    override val viewModel: DashboardViewModel by viewModels()

    private lateinit var mContext: Context

    private var dataList = emptyList<Data>()
    private var position: Int = 0

    override fun inflateView(
        inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentDashboardBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAppData()
    }

    override fun setUI() {
        binding.loadingScreen.root.isVisible
        (activity as MainActivity).showBanner()
    }

    override fun setListeners() {
        binding.apply {
            btnYes.setOnClickListener {  }
            btnNo.setOnClickListener {  }

            btnNext.setOnClickListener {
                position++
                if(position<dataList.size){
                    binding.loadingScreen.root.isVisible
                    val data = dataList[position]
                    viewModel.getPercents(data.votesYes, data.votesNo)
                }
            }
        }
    }

    override fun observe() {
        viewModel.event.observe(this) { event ->
            when (event) {
                is DashboardEvent.AppData -> {
                    dataList = event.data
                    getPercents()
                }
                is DashboardEvent.GetPercents -> setData(event.percents)
                is DashboardEvent.SWW -> error()
            }
        }
    }

    private fun getPercents() {
        try {
            val data = dataList[position]
            viewModel.getPercents(data.votesYes, data.votesNo)
        } catch (e: Exception) {
            error()
        }
    }

    private fun setData(dataPercents: Percents) {
        binding.apply {
            try {
                val data = dataList[position]
                tvTextTop.text = data.topText
                tvTextBottom.text = data.bottomText
                percents.apply {
                    tvPercentYes.text = dataPercents.percentYes
                    tvPercentNo.text = dataPercents.percentNo
                    tvVotesYes.text = data.votesYes.toString()
                    tvVotesNo.text = data.votesNo.toString()
                }
                loadingScreen.root.isVisible = false
            } catch (e: java.lang.Exception) {
                error()
            }
        }
    }

    private fun error() {
        activity?.showErrorDialog() { activity?.finish() }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
}
//    override val viewModel: DashboardViewModel by viewModels()
//
//    private lateinit var mContext: Context
//
//    override fun inflateView(
//        inflater: LayoutInflater, container: ViewGroup?
//    ) = FragmentCategoriesBinding.inflate(inflater, container, false)
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        viewModel.getCategories()
//    }
//
//    override fun setUI() {
//        binding.apply {
//            setUpToolbar()
//            loading.root.isVisible
//        }
//    }
//
//    private fun setUpToolbar() {
//        (activity as MainActivity).apply {
//            showBanner(visible = false)
//            toolbarIcon(showDuck = true)
//            toolbarText(getString(R.string.app_name))
//        }
//    }
//
//    override fun observe() {
//        viewModel.event.observe(this) { event ->
//            when (event) {
//                is DashboardEvent.Dashboard -> setCategoryRecycler(event.result)
//                is DashboardEvent.SomethingWentWrong -> activity?.showErrorDialog()
//            }
//        }
//    }
//
//    private fun setCategoryRecycler(categories: List<Category>) {
//        if (categories.isNotEmpty()) {
//            binding.rvCategory.apply {
//                layoutManager = LinearLayoutManager(mContext)
//                adapter =
//                    CategoriesAdapter(categories.sortedBy { it.order }, this@DashboardFragment)
//            }
//            binding.loading.root.isVisible = false
//        }
//    }
//
//    override fun onCategoryClick(id: String) {
//        if (id == getString(R.string.music)) {
//            startActivity(Intent(ACTION_VIEW, Uri.parse(getString(R.string.music_master_url))))
//        } else findNavController().navigate(actionCategoriesToDashboard(id))
//    }
//
//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        mContext = context
//    }
//}