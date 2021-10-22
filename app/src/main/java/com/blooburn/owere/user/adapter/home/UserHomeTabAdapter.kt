package com.blooburn.owere.user.adapter.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.blooburn.owere.user.fragment.mainFragment.homeFragment.UserHomeDesignerFragment
import com.blooburn.owere.user.fragment.mainFragment.homeFragment.UserHomeSalonFragment

class UserHomeTabAdapter(fragment: Fragment, private var tabCount: Int) :
    FragmentStateAdapter(fragment) {

    private val fragmentList =
        mutableListOf(UserHomeDesignerFragment(), UserHomeSalonFragment())

    override fun getItemCount(): Int {
        return tabCount
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}