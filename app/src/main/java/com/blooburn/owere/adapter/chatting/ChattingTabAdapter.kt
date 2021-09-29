package com.blooburn.owere.adapter.chatting

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.blooburn.owere.fragment.userMainFragment.chattingFragment.ChattingDesignerFragment
import com.blooburn.owere.fragment.userMainFragment.chattingFragment.ChattingSalonFragment

//탭 레이아웃을 위한 어답터
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