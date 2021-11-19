package com.blooburn.owere.designer.adapter.chatting

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.blooburn.owere.designer.fragment.chatting.ChattingUserFragment
import com.blooburn.owere.user.fragment.mainFragment.chattingFragment.ChattingDesignerFragment
import com.blooburn.owere.user.fragment.mainFragment.chattingFragment.ChattingSalonFragment

//탭 레이아웃을 위한 어답터
class ChattingTabAdapterDesigner (fragment: Fragment, private var tabCount : Int)
    : FragmentStateAdapter(fragment) {



    override fun getItemCount(): Int {
        return tabCount
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            //유저와의 채팅 리스트 프래그먼트
            0 -> ChattingUserFragment()
            //미용실과의 채팅 리스트 프래그먼트
            else -> ChattingSalonFragment()

        }
    }
}