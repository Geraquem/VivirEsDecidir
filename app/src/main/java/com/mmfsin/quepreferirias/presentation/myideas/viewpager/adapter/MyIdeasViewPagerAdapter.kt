package com.mmfsin.quepreferirias.presentation.myideas.viewpager.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mmfsin.quepreferirias.presentation.myideas.dilemmas.MyDilemmasFragment
import com.mmfsin.quepreferirias.presentation.myideas.interfaces.IMyIdeasListener

class MyIdeasViewPagerAdapter(
    fragmentActivity: FragmentActivity,
    val listener: IMyIdeasListener
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MyDilemmasFragment(listener)
            else -> MyDilemmasFragment(listener)
        }
    }
}