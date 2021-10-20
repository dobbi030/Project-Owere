package com.blooburn.owere.user.adapter.userReservation

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.blooburn.owere.user.fragment.mainFragment.reservationFragment.ConfirmedFragment
import com.blooburn.owere.user.fragment.mainFragment.reservationFragment.WaitingFragment

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