package com.mmfsin.quepreferirias.presentation.ideas.myideas.viewpager.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mmfsin.quepreferirias.presentation.ideas.myideas.dilemmas.MyDilemmasFragment
import com.mmfsin.quepreferirias.presentation.ideas.interfaces.IIdeasListener

class MyIdeasViewPagerAdapter(
    fragmentActivity: FragmentActivity,
    val listener: IIdeasListener
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MyDilemmasFragment(listener)
            else -> MyDilemmasFragment(listener)
        }
    }
}