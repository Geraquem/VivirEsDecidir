package com.mmfsin.quepreferirias.presentation.ideas.myideas.viewpager

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.mmfsin.quepreferirias.R
import com.mmfsin.quepreferirias.base.BaseFragmentNoVM
import com.mmfsin.quepreferirias.databinding.FragmentViewpagerBinding
import com.mmfsin.quepreferirias.presentation.ideas.interfaces.IIdeasListener
import com.mmfsin.quepreferirias.presentation.ideas.myideas.viewpager.MyIdeasViewPagerFragmentDirections.Companion.myIdeasToSingleDilemma
import com.mmfsin.quepreferirias.presentation.ideas.myideas.viewpager.adapter.MyIdeasViewPagerAdapter
import com.mmfsin.quepreferirias.presentation.main.BedRockActivity
import com.mmfsin.quepreferirias.utils.showErrorDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyIdeasViewPagerFragment : BaseFragmentNoVM<FragmentViewpagerBinding>(), IIdeasListener {

    private lateinit var mContext: Context

    override fun inflateView(
        inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentViewpagerBinding.inflate(inflater, container, false)

    override fun setUI() {
        binding.apply {
            loading.root.visibility = View.VISIBLE
            setToolbar()
            setUpViewPager()
        }
    }

    private fun setToolbar() {
        (activity as BedRockActivity).apply {
            backListener { onBackPressed() }
            setToolbarText(R.string.my_data_title)
        }
    }

    private fun setUpViewPager() {
        binding.apply {
            activity?.let {
                viewPager.adapter = MyIdeasViewPagerAdapter(it, this@MyIdeasViewPagerFragment)
                TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                    when (position) {
                        0 -> tab.setText(R.string.my_data_dilemmas)
                        1 -> tab.setText(R.string.my_data_dualisms)
                    }
                }.attach()
                loading.root.visibility = View.GONE
            }
        }
    }

    override fun navigateToSingleDilemma(dilemmaId: String) =
        findNavController().navigate(myIdeasToSingleDilemma(dilemmaId))

    override fun navigateToSingleDualism(dualismId: String) {
        TODO("Not yet implemented")
    }

    private fun error() {
        activity?.showErrorDialog { activity?.finish() }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
}