package com.mmfsin.quepreferirias.presentation.ideas.otherideas.viewpager

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
import com.mmfsin.quepreferirias.presentation.ideas.otherideas.viewpager.OtherIdeasViewPagerFragmentDirections.Companion.otherIdeasToSingleDilemma
import com.mmfsin.quepreferirias.presentation.ideas.otherideas.viewpager.adapter.OtherIdeasViewPagerAdapter
import com.mmfsin.quepreferirias.presentation.main.BedRockActivity
import com.mmfsin.quepreferirias.utils.POSITION
import com.mmfsin.quepreferirias.utils.USER_ID
import com.mmfsin.quepreferirias.utils.showErrorDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OtherIdeasViewPagerFragment : BaseFragmentNoVM<FragmentViewpagerBinding>(),
    IIdeasListener {

    private lateinit var mContext: Context

    private var userId: String? = null
    private var tabPosition: Int = 0

    override fun inflateView(
        inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentViewpagerBinding.inflate(inflater, container, false)

    override fun getBundleArgs() {
        arguments?.let {
            userId = it.getString(USER_ID)
            tabPosition = it.getInt(POSITION, 0)
        }
    }

    override fun setUI() {
        binding.apply {
            loading.root.visibility = View.VISIBLE
            setToolbar()
            userId?.let { id -> setUpViewPager(userId = id) } ?: run { error() }
        }
    }

    private fun setToolbar() {
        (activity as BedRockActivity).apply {
            backListener { onBackPressed() }
        }
    }

    private fun setUpViewPager(userId: String) {
        binding.apply {
            activity?.let {
                viewPager.adapter = OtherIdeasViewPagerAdapter(
                    it,
                    userId = userId,
                    listener = this@OtherIdeasViewPagerFragment
                )
                TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                    when (position) {
                        0 -> tab.setText(R.string.my_data_dilemmas)
                        1 -> tab.setText(R.string.my_data_dualisms)
                    }
                }.attach()


                viewPager.setCurrentItem(tabPosition, false)
                loading.root.visibility = View.GONE
            }
        }
    }

    override fun navigateToSingleDilemma(dilemmaId: String) =
        findNavController().navigate(otherIdeasToSingleDilemma(dilemmaId))

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