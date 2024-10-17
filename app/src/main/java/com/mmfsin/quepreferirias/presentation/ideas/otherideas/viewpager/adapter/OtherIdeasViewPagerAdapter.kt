package com.mmfsin.quepreferirias.presentation.ideas.otherideas.viewpager.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mmfsin.quepreferirias.presentation.ideas.interfaces.IIdeasListener
import com.mmfsin.quepreferirias.presentation.ideas.otherideas.dilemmas.OtherDilemmasFragment
import com.mmfsin.quepreferirias.presentation.ideas.otherideas.dualisms.OtherDualismsFragment

class OtherIdeasViewPagerAdapter(
    fragmentActivity: FragmentActivity,
    val userId: String,
    val listener: IIdeasListener
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OtherDilemmasFragment(userId, listener)
            else -> OtherDualismsFragment(userId, listener)
        }
    }
}