package com.blooburn.owere.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.blooburn.owere.fragment.userMainFragment.chattingFragment.ChattingDesignerFragment
import com.blooburn.owere.fragment.userMainFragment.chattingFragment.ChattingSalonFragment

class ChattingTabAdapter(fragment: Fragment, private var tabCount : Int)
    : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return tabCount
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> ChattingDesignerFragment()
            else -> ChattingSalonFragment()

        }
    }
}