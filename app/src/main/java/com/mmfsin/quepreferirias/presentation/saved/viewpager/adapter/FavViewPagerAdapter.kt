package com.mmfsin.quepreferirias.presentation.saved.viewpager.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mmfsin.quepreferirias.presentation.saved.dilemmas.DilemmaFavFragment
import com.mmfsin.quepreferirias.presentation.saved.listeners.IViewPagerListener

class FavViewPagerAdapter(fragmentActivity: FragmentActivity, val listener: IViewPagerListener) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> DilemmaFavFragment(listener)
            else -> DilemmaFavFragment(listener)
        }
    }
}