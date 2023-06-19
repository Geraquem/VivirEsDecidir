package com.mmfsin.quepreferirias.presentation.dashboard

import android.graphics.drawable.AnimationDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.mmfsin.quepreferirias.base.BaseFragment
import com.mmfsin.quepreferirias.databinding.FragmentDashboardBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment : BaseFragment<FragmentDashboardBinding, DashboardViewModel>() {

    override val viewModel: DashboardViewModel by viewModels()

    override fun inflateView(
        inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentDashboardBinding.inflate(inflater, container, false)


    override fun setUI() {
        binding.apply {
            val animationDrawable = clMain.background as AnimationDrawable
            animationDrawable.setEnterFadeDuration(6000)
            animationDrawable.setExitFadeDuration(6000)
            animationDrawable.start()
        }
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