package com.blooburn.owere.adapter.userReservation

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.blooburn.owere.fragment.userMainFragment.reservationFragment.ConfirmedFragment
import com.blooburn.owere.fragment.userMainFragment.reservationFragment.WaitingFragment

class ScheduledTabAdapter (fragment : Fragment, private var tabCount : Int)
    : FragmentStateAdapter(fragment){

    private val fragmentList = mutableListOf(ConfirmedFragment(),WaitingFragment())

    override fun getItemCount(): Int {
        return tabCount
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}