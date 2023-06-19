package com.mmfsin.quepreferirias.presentation.dashboard

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.mmfsin.quepreferirias.R
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

    private var votesYes: Long = 0
    private var votesNo: Long = 0

    override fun inflateView(
        inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentDashboardBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAppData()
    }

    override fun setUI() {
        binding.apply {
            loadingScreen.root.isVisible
            votesYes = 0
            votesNo = 0
            percents.root.visibility = View.INVISIBLE
        }
        (activity as MainActivity).showBanner()
    }

    override fun setListeners() {
        binding.apply {
            btnYes.setOnClickListener { yesOrNoClick(isYes = true) }
            btnNo.setOnClickListener { yesOrNoClick(isYes = false) }

            btnNext.setOnClickListener {
                position++
                if (position < dataList.size) {
                    binding.loadingScreen.root.isVisible
                    setData()
                }
            }
        }
    }

    private fun yesOrNoClick(isYes: Boolean) {
        binding.apply {
            try {
                votesYes = dataList[position].votesYes
                votesNo = dataList[position].votesNo
                if (isYes) votesYes += 1 else votesNo += 1
                viewModel.getPercents(votesYes, votesNo)
                if (isYes) btnYes.setImageResource(R.drawable.ic_option_yes)
                else btnNo.setImageResource(R.drawable.ic_option_no)
                btnYes.isEnabled = false
                btnNo.isEnabled = false
            } catch (e: java.lang.Exception) {
                error()
            }
        }
    }

    override fun observe() {
        viewModel.event.observe(this) { event ->
            when (event) {
                is DashboardEvent.AppData -> {
                    dataList = event.data
                    setData()
                }
                is DashboardEvent.GetPercents -> setPercents(event.percents)
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

    private fun setData() {
        binding.apply {
            try {
                votesYes = 0
                votesNo = 0
                val data = dataList[position]
                percents.root.visibility = View.INVISIBLE
                tvTextTop.text = data.topText
                tvTextBottom.text = data.bottomText
                btnYes.setImageResource(R.drawable.ic_option_yes_trans)
                btnNo.setImageResource(R.drawable.ic_option_no_trans)
                btnYes.isEnabled = true
                btnNo.isEnabled = true
                loadingScreen.root.isVisible = false
            } catch (e: java.lang.Exception) {
                error()
            }
        }
    }

    private fun setPercents(actualPercents: Percents) {
        binding.apply {
            try {
                val data = dataList[position]
                percents.apply {
                    tvPercentYes.text = actualPercents.percentYes
                    tvPercentNo.text = actualPercents.percentNo
                    tvVotesYes.text = votesYes.toString()
                    tvVotesNo.text = votesNo.toString()
                    root.visibility = View.VISIBLE
                }
            } catch (e: Exception) {
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