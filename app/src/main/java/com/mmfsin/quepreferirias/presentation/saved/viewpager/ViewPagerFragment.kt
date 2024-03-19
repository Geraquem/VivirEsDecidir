package com.mmfsin.quepreferirias.presentation.saved.viewpager

import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.mmfsin.quepreferirias.R
import com.mmfsin.quepreferirias.base.BaseFragment
import com.mmfsin.quepreferirias.base.BaseFragmentNoVM
import com.mmfsin.quepreferirias.databinding.FragmentSendDilemmaBinding
import com.mmfsin.quepreferirias.databinding.FragmentViewpagerBinding
import com.mmfsin.quepreferirias.domain.models.Session
import com.mmfsin.quepreferirias.presentation.main.BedRockActivity
import com.mmfsin.quepreferirias.presentation.saved.viewpager.adapter.ViewPagerAdapter
import com.mmfsin.quepreferirias.presentation.send.dialogs.SendDataResultDialog
import com.mmfsin.quepreferirias.presentation.send.interfaces.ISendDataResultListener
import com.mmfsin.quepreferirias.presentation.send.interfaces.TextWatcher
import com.mmfsin.quepreferirias.utils.showErrorDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewPagerFragment : BaseFragmentNoVM<FragmentViewpagerBinding>() {

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
            setToolbarText(R.string.saved_data_title)
        }
    }

    private fun setUpViewPager() {
        binding.apply {
            activity?.let {
                viewPager.adapter = ViewPagerAdapter(it)
                TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                    when (position) {
                        0 -> tab.setText(R.string.saved_data_dilemmas)
                        1 -> tab.setText(R.string.saved_data_dualisms)
                    }
                }.attach()
                loading.root.visibility = View.GONE
            }
        }
    }

    private fun error() {
        activity?.showErrorDialog { activity?.finish() }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
}