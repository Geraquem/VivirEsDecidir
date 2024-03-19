package com.mmfsin.quepreferirias.presentation.myideas.viewpager.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mmfsin.quepreferirias.presentation.myideas.dilemmas.MyDilemmasFragment

class MyIdeasViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MyDilemmasFragment()
            else -> MyDilemmasFragment()
        }
    }
}