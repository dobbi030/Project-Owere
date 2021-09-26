package com.example.owere.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.owere.fragment.userMainFragment.homeFragment.UserHomeDesignerFragment
import com.example.owere.fragment.userMainFragment.homeFragment.UserHomeSalonFragment

class UserHomeTabAdapter(fragment: Fragment, private var tabCount: Int) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return tabCount
    }

    override fun createFragment(position: Int): Fragment {
        return when (position){
            0 -> UserHomeDesignerFragment()
            else -> UserHomeSalonFragment()
        }
    }
}